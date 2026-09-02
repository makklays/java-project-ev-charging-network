package com.techmatrix18.charging_invoice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ChargingInvoice
 * Чистая доменная сущность бухгалтерского счета за зарядную сессию (FinTech ядро)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public class ChargingInvoice {

    private final Long id;
    private final Long sessionId;
    private final Long userId;
    private final String invoiceNumber;
    private InvoiceStatus status;

    // Временные показатели
    private final Integer chargingDurationMinutes;
    private final Integer idleMinutes;
    private final Integer totalSessionDurationMinutes;

    // Объемы и Финансы
    private final BigDecimal consumedKwh;
    private final BigDecimal energyAmount;
    private final BigDecimal idleAmount;
    private final BigDecimal vatAmount;
    private final BigDecimal finalAmountWithVat;

    // Метки времени и аудит
    private final ZonedDateTime issuedAt;
    private ZonedDateTime paidAt;
    private final Long version;

    // Список событий, произошедших с агрегатом в памяти
    private final List<Object> domainEvents = new ArrayList<>();

    /**
     * Конструктор для первичного формирования инвойса (Вызывается биллингом при закрытии сессии)
     */
    public ChargingInvoice(Long sessionId, Long userId, String invoiceNumber,
                           Integer chargingDurationMinutes, Integer idleMinutes,
                           BigDecimal consumedKwh, BigDecimal energyAmount, BigDecimal idleAmount) {
        this.id = null; // Генерируется базой данных (BIGSERIAL)
        this.sessionId = Objects.requireNonNull(sessionId, "Session ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");

        this.invoiceNumber = Objects.requireNonNull(invoiceNumber, "Invoice number cannot be null");
        if (invoiceNumber.isBlank()) throw new IllegalArgumentException("Invoice number cannot be empty");

        this.status = InvoiceStatus.ISSUED; // По умолчанию из миграции

        this.chargingDurationMinutes = Objects.requireNonNull(chargingDurationMinutes, "Charging duration required");
        this.idleMinutes = Objects.requireNonNull(idleMinutes, "Idle minutes required");
        this.totalSessionDurationMinutes = chargingDurationMinutes + idleMinutes;

        this.consumedKwh = Objects.requireNonNull(consumedKwh, "Consumed kWh cannot be null");
        this.energyAmount = Objects.requireNonNull(energyAmount, "Energy amount cannot be null");
        this.idleAmount = Objects.requireNonNull(idleAmount, "Idle amount cannot be null");

        // Автоматический расчет НДС (20% в Украине) от суммы за энергию и простой
        BigDecimal baseAmount = energyAmount.add(idleAmount);
        this.vatAmount = baseAmount.multiply(new BigDecimal("0.20")).setScale(4, RoundingMode.HALF_UP);
        this.finalAmountWithVat = baseAmount.add(this.vatAmount).setScale(4, RoundingMode.HALF_UP);

        this.issuedAt = ZonedDateTime.now();
        this.paidAt = null;
        this.version = 0L;

        validateFinancialLimits();
    }

    /**
     * Конструктор для восстановления сущности из базы данных (Маппинг инфраструктуры)
     */
    public ChargingInvoice(Long id, Long sessionId, Long userId, String invoiceNumber, InvoiceStatus status,
                           Integer chargingDurationMinutes, Integer idleMinutes, Integer totalSessionDurationMinutes,
                           BigDecimal consumedKwh, BigDecimal energyAmount, BigDecimal idleAmount,
                           BigDecimal vatAmount, BigDecimal finalAmountWithVat, ZonedDateTime issuedAt,
                           ZonedDateTime paidAt, Long version) {
        this.id = Objects.requireNonNull(id, "Invoice ID cannot be null");
        this.sessionId = Objects.requireNonNull(sessionId, "Session ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.invoiceNumber = Objects.requireNonNull(invoiceNumber, "Invoice number cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.chargingDurationMinutes = Objects.requireNonNull(chargingDurationMinutes, "Charging duration required");
        this.idleMinutes = Objects.requireNonNull(idleMinutes, "Idle minutes required");
        this.totalSessionDurationMinutes = Objects.requireNonNull(totalSessionDurationMinutes, "Total duration required");
        this.consumedKwh = Objects.requireNonNull(consumedKwh, "Consumed kWh cannot be null");
        this.energyAmount = Objects.requireNonNull(energyAmount, "Energy amount cannot be null");
        this.idleAmount = Objects.requireNonNull(idleAmount, "Idle amount cannot be null");
        this.vatAmount = Objects.requireNonNull(vatAmount, "VAT amount cannot be null");
        this.finalAmountWithVat = Objects.requireNonNull(finalAmountWithVat, "Final amount with VAT cannot be null");
        this.issuedAt = Objects.requireNonNull(issuedAt, "IssuedAt date cannot be null");
        this.paidAt = paidAt;
        this.version = Objects.requireNonNull(version, "Version cannot be null");

        validateFinancialLimits();
    }

    // --- Инкапсулированные бизнес-методы (Rich Domain Model) ---

    /**
     * Фиксация успешного списания денег с кошелька пользователя
     */
    public void markAsPaid() {
        if (this.status == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice is already paid");
        }
        this.status = InvoiceStatus.PAID;
        this.paidAt = ZonedDateTime.now();
    }

    /**
     * Фиксация ошибки списания средств (например, недостаточно денег на балансе кошелька)
     */
    public void markAsFailed() {
        if (this.status == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot fail an already successfully paid invoice");
        }
        this.status = InvoiceStatus.FAILED;
    }

    /**
     * Перевод инвойса в статус диспута/оспаривания водителем через службу поддержки
     */
    public void dispute() {
        this.status = InvoiceStatus.DISPUTED;
    }

    private void validateFinancialLimits() {
        if (this.consumedKwh.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Consumed electricity volume cannot be negative");
        }
        if (this.energyAmount.compareTo(BigDecimal.ZERO) < 0 || this.idleAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invoice cost amounts cannot be negative values");
        }
        if (this.chargingDurationMinutes < 0 || this.idleMinutes < 0) {
            throw new IllegalArgumentException("Time indicators cannot hold negative duration values");
        }
    }

    /**
     * Возвращает неизменяемый список произошедших доменных событий.
     */
    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Очищает список событий после их успешного сохранения инфраструктурным адаптером.
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    // --- Геттеры для портов и мапперов ---

    public Long getId() { return id; }
    public Long getSessionId() { return sessionId; }
    public Long getUserId() { return userId; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public InvoiceStatus getStatus() { return status; }
    public Integer getChargingDurationMinutes() { return chargingDurationMinutes; }
    public Integer getIdleMinutes() { return idleMinutes; }
    public Integer getTotalSessionDurationMinutes() { return totalSessionDurationMinutes; }
    public BigDecimal getConsumedKwh() { return consumedKwh; }
    public BigDecimal getEnergyAmount() { return energyAmount; }
    public BigDecimal getIdleAmount() { return idleAmount; }
    public BigDecimal getVatAmount() { return vatAmount; }
    public BigDecimal getFinalAmountWithVat() { return finalAmountWithVat; }
    public ZonedDateTime getIssuedAt() { return issuedAt; }
    public ZonedDateTime getPaidAt() { return paidAt; }
    public Long getVersion() { return version; }
}

