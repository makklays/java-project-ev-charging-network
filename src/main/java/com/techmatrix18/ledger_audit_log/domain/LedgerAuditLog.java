package com.techmatrix18.ledger_audit_log.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * BillingLedgerAuditLog
 * Чистая доменная сущность неизменяемой бухгалтерской проводки (Ledger Audit Entry)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

public class LedgerAuditLog {
    private final Long id;
    private final Long userId;
    private final Long chargingInvoiceId;
    private final String operationType;
    private final BigDecimal amount;
    private final BigDecimal walletBalanceSnapshot;
    private final BigDecimal historicalPricePerKwh;
    private final String historicalTariffName;
    private final BigDecimal deltaKwh;
    private final Integer deltaMinutes;
    private final BigDecimal totalMeterKwh;
    private final String auditComment;
    private final ZonedDateTime createdAt;
    private final String createdBy;

    // Список событий, произошедших с агрегатом в памяти
    private final List<Object> domainEvents = new ArrayList<>();

    public LedgerAuditLog(Long userId, Long chargingInvoiceId, String operationType,
                          BigDecimal amount, BigDecimal walletBalanceSnapshot, String auditComment) {
        this.id = null; // Будет сгенерирован базой данных
        this.userId = userId;
        this.chargingInvoiceId = chargingInvoiceId;
        this.operationType = operationType;
        this.amount = amount;
        this.walletBalanceSnapshot = walletBalanceSnapshot;
        this.historicalPricePerKwh = BigDecimal.ZERO;
        this.historicalTariffName = "DEFAULT_TARIFF";
        this.deltaKwh = BigDecimal.ZERO;
        this.deltaMinutes = 0;
        this.totalMeterKwh = BigDecimal.ZERO;
        this.auditComment = auditComment;
        this.createdAt = ZonedDateTime.now();
        this.createdBy = "SYSTEM";

        // Также фиксируем доменное событие, которое мы настраивали ранее
        this.getDomainEvents().add(new LedgerEntryLoggedEvent(this.id, this.operationType, this.amount));
    }

    // Конструктор для первичного создания проводки (Вызывается биллинг-движком при фиксации списания/начисления)
    public LedgerAuditLog(Long userId, Long chargingInvoiceId, String operationType, BigDecimal amount,
                          BigDecimal walletBalanceSnapshot, BigDecimal historicalPricePerKwh,
                          String historicalTariffName, BigDecimal deltaKwh, Integer deltaMinutes,
                          BigDecimal totalMeterKwh, String auditComment) {
        this.id = null; // Генерируется СУБД (BIGSERIAL)
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.chargingInvoiceId = Objects.requireNonNull(chargingInvoiceId, "Invoice ID cannot be null");
        this.operationType = Objects.requireNonNull(operationType, "Operation type cannot be null");
        this.amount = Objects.requireNonNull(amount, "Transaction amount cannot be null");
        this.walletBalanceSnapshot = Objects.requireNonNull(walletBalanceSnapshot, "Wallet balance snapshot cannot be null");

        // Исторические поля тарифа могут быть null (например, при финальном закрытии инвойса, а не тике энергии)
        this.historicalPricePerKwh = historicalPricePerKwh;
        this.historicalTariffName = historicalTariffName;

        this.deltaKwh = Objects.requireNonNull(deltaKwh, "Delta kWh volume cannot be null");
        this.deltaMinutes = Objects.requireNonNull(deltaMinutes, "Delta minutes cannot be null");
        this.totalMeterKwh = Objects.requireNonNull(totalMeterKwh, "Total meter value cannot be null");
        this.auditComment = auditComment;

        this.createdAt = ZonedDateTime.now();
        this.createdBy = "BILLING_ENGINE_V1"; // Константа компонента-эмитента

        validateLedgerConstraints();
    }

    // Конструктор для восстановления сущности из базы данных (Маппинг инфраструктуры)
    public LedgerAuditLog(Long id, Long userId, Long chargingInvoiceId, String operationType, BigDecimal amount,
                          BigDecimal walletBalanceSnapshot, BigDecimal historicalPricePerKwh,
                          String historicalTariffName, BigDecimal deltaKwh, Integer deltaMinutes,
                          BigDecimal totalMeterKwh, String auditComment, ZonedDateTime createdAt, String createdBy) {
        this.id = Objects.requireNonNull(id, "Ledger ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.chargingInvoiceId = Objects.requireNonNull(chargingInvoiceId, "Invoice ID cannot be null");
        this.operationType = Objects.requireNonNull(operationType, "Operation type cannot be null");
        this.amount = Objects.requireNonNull(amount, "Transaction amount cannot be null");
        this.walletBalanceSnapshot = Objects.requireNonNull(walletBalanceSnapshot, "Wallet balance snapshot cannot be null");
        this.historicalPricePerKwh = historicalPricePerKwh;
        this.historicalTariffName = historicalTariffName;
        this.deltaKwh = Objects.requireNonNull(deltaKwh, "Delta kWh volume cannot be null");
        this.deltaMinutes = Objects.requireNonNull(deltaMinutes, "Delta minutes cannot be null");
        this.totalMeterKwh = Objects.requireNonNull(totalMeterKwh, "Total meter value cannot be null");
        this.auditComment = auditComment;
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt timestamp cannot be null");
        this.createdBy = Objects.requireNonNull(createdBy, "CreatedBy issuer system cannot be null");

        validateLedgerConstraints();
    }

    private void validateLedgerConstraints() {
        if (this.operationType.isBlank()) {
            throw new IllegalArgumentException("Ledger financial operation type cannot be empty");
        }
        if (this.deltaKwh.compareTo(BigDecimal.ZERO) < 0 || this.totalMeterKwh.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Physical meter metric deltas cannot hold negative values");
        }
        if (this.deltaMinutes < 0) {
            throw new IllegalArgumentException("Time delta cannot be negative");
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

    // --- Геттеры для портов и мапперов (Иммутабельность сохранена, сеттеров нет) ---

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getChargingInvoiceId() { return chargingInvoiceId; }
    public String getOperationType() { return operationType; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getWalletBalanceSnapshot() { return walletBalanceSnapshot; }
    public BigDecimal getHistoricalPricePerKwh() { return historicalPricePerKwh; }
    public String getHistoricalTariffName() { return historicalTariffName; }
    public BigDecimal getDeltaKwh() { return deltaKwh; }
    public Integer getDeltaMinutes() { return deltaMinutes; }
    public BigDecimal getTotalMeterKwh() { return totalMeterKwh; }
    public String getAuditComment() { return auditComment; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public String getCreatedBy() { return createdBy; }
}

