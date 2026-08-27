package com.techmatrix18.charging_invoice.infrastructure.db;

import com.techmatrix18.charging_invoice.domain.ChargingInvoice;
import com.techmatrix18.charging_invoice.domain.InvoiceStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * ChargingInvoiceEntity
 * JPA Сущность для таблицы "charging_invoices" (Слой инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Entity
@Table(
        name = "charging_invoices",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_invoices_session", columnNames = {"session_id"}),
                @UniqueConstraint(name = "uk_invoices_number", columnNames = {"invoice_number"})
        }
)
public class ChargingInvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "invoice_number", nullable = false, length = 50)
    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private InvoiceStatus status;

    @Column(name = "charging_duration_minutes", nullable = false)
    private Integer chargingDurationMinutes;

    @Column(name = "idle_minutes", nullable = false)
    private Integer idleMinutes;

    @Column(name = "total_session_duration_minutes", nullable = false)
    private Integer totalSessionDurationMinutes;

    @Column(name = "consumed_kwh", nullable = false, precision = 12, scale = 3)
    private BigDecimal consumedKwh;

    @Column(name = "energy_amount", nullable = false, precision = 12, scale = 4)
    private BigDecimal energyAmount;

    @Column(name = "idle_amount", nullable = false, precision = 12, scale = 4)
    private BigDecimal idleAmount;

    @Column(name = "vat_amount", nullable = false, precision = 12, scale = 4)
    private BigDecimal vatAmount;

    @Column(name = "final_amount_with_vat", nullable = false, precision = 12, scale = 4)
    private BigDecimal finalAmountWithVat;

    @Column(name = "issued_at", nullable = false)
    private ZonedDateTime issuedAt;

    @Column(name = "paid_at")
    private ZonedDateTime paidAt;

    @Version // Активирует Optimistic Locking на основе поля version BIGINT из вашей миграции
    @Column(nullable = false)
    private Long version;

    // --- Автоматическое управление временными метками жизненного цикла JPA ---
    @PrePersist
    protected void onCreate() {
        if (this.issuedAt == null) {
            this.issuedAt = ZonedDateTime.now();
        }
    }

    // --- Конструкторы ---
    public ChargingInvoiceEntity() {
    }

    // --- Маппинг: Из чистого Домена в JPA Entity (Для сохранения в БД) ---
    public static ChargingInvoiceEntity fromDomain(ChargingInvoice domain) {
        if (domain == null) return null;

        ChargingInvoiceEntity entity = new ChargingInvoiceEntity();
        entity.id = domain.getId();
        entity.sessionId = domain.getSessionId();
        entity.userId = domain.getUserId();
        entity.invoiceNumber = domain.getInvoiceNumber();
        entity.status = domain.getStatus();
        entity.chargingDurationMinutes = domain.getChargingDurationMinutes();
        entity.idleMinutes = domain.getIdleMinutes();
        entity.totalSessionDurationMinutes = domain.getTotalSessionDurationMinutes();
        entity.consumedKwh = domain.getConsumedKwh();
        entity.energyAmount = domain.getEnergyAmount();
        entity.idleAmount = domain.getIdleAmount();
        entity.vatAmount = domain.getVatAmount();
        entity.finalAmountWithVat = domain.getFinalAmountWithVat();
        entity.issuedAt = domain.getIssuedAt();
        entity.paidAt = domain.getPaidAt();
        entity.version = domain.getVersion(); // Передаем текущую версию для контроля блокировок
        return entity;
    }

    // --- Маппинг: Из JPA Entity в чистый Домен (Для чтения из БД) ---
    public ChargingInvoice toDomain() {
        return new ChargingInvoice(
                this.id,
                this.sessionId,
                this.userId,
                this.invoiceNumber,
                this.status,
                this.chargingDurationMinutes,
                this.idleMinutes,
                this.totalSessionDurationMinutes,
                this.consumedKwh,
                this.energyAmount,
                this.idleAmount,
                this.vatAmount,
                this.finalAmountWithVat,
                this.issuedAt,
                this.paidAt,
                this.version // Передаем техническую версию обратно в доменное ядро
        );
    }

    // --- Стандартные геттеры и сеттеры для JPA ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public InvoiceStatus getStatus() { return status; }
    public void setStatus(InvoiceStatus status) { this.status = status; }

    public Integer getChargingDurationMinutes() { return chargingDurationMinutes; }
    public void setChargingDurationMinutes(Integer chargingDurationMinutes) { this.chargingDurationMinutes = chargingDurationMinutes; }

    public Integer getIdleMinutes() { return idleMinutes; }
    public void setIdleMinutes(Integer idleMinutes) { this.idleMinutes = idleMinutes; }

    public Integer getTotalSessionDurationMinutes() { return totalSessionDurationMinutes; }
    public void setTotalSessionDurationMinutes(Integer totalSessionDurationMinutes) { this.totalSessionDurationMinutes = totalSessionDurationMinutes; }

    public BigDecimal getConsumedKwh() { return consumedKwh; }
    public void setConsumedKwh(BigDecimal consumedKwh) { this.consumedKwh = consumedKwh; }

    public BigDecimal getEnergyAmount() { return energyAmount; }
    public void setEnergyAmount(BigDecimal energyAmount) { this.energyAmount = energyAmount; }

    public BigDecimal getIdleAmount() { return idleAmount; }
    public void setIdleAmount(BigDecimal idleAmount) { this.idleAmount = idleAmount; }

    public BigDecimal getVatAmount() { return vatAmount; }
    public void setVatAmount(BigDecimal vatAmount) { this.vatAmount = vatAmount; }

    public BigDecimal getFinalAmountWithVat() { return finalAmountWithVat; }
    public void setFinalAmountWithVat(BigDecimal finalAmountWithVat) { this.finalAmountWithVat = finalAmountWithVat; }

    public ZonedDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(ZonedDateTime issuedAt) { this.issuedAt = issuedAt; }

    public ZonedDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(ZonedDateTime paidAt) { this.paidAt = paidAt; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}

