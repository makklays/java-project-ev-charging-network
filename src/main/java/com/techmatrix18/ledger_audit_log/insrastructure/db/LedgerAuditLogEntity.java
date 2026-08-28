package com.techmatrix18.ledger_audit_log.insrastructure.db;

import com.techmatrix18.ledger_audit_log.domain.LedgerAuditLog;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * BillingLedgerAuditLogEntity
 * JPA Сущность для таблицы "billing_ledger_audit_log" (Слой инфраструктуры)
 *
 * Строго Append-Only: аннотации @Version и @PreUpdate отсутствуют, так как строки неизменяемы.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

@Entity
@Table(name = "billing_ledger_audit_log")
public class LedgerAuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "charging_invoice_id", nullable = false)
    private Long chargingInvoiceId;

    @Column(name = "operation_type", nullable = false, length = 50)
    private String operationType;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal amount;

    @Column(name = "wallet_balance_snapshot", nullable = false, precision = 12, scale = 4)
    private BigDecimal walletBalanceSnapshot;

    @Column(name = "historical_price_per_kwh", precision = 10, scale = 4)
    private BigDecimal historicalPricePerKwh;

    @Column(name = "historical_tariff_name", length = 50)
    private String historicalTariffName;

    @Column(name = "delta_kwh", nullable = false, precision = 12, scale = 3)
    private BigDecimal deltaKwh;

    @Column(name = "delta_minutes", nullable = false)
    private Integer deltaMinutes;

    @Column(name = "total_meter_kwh", nullable = false, precision = 12, scale = 3)
    private BigDecimal totalMeterKwh;

    @Column(name = "audit_comment", length = 255)
    private String auditComment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = ZonedDateTime.now();
        }
    }

    // --- Конструкторы ---

    public LedgerAuditLogEntity() {
    }

    // --- Маппинг: Домен -> JPA Entity ---

    public static LedgerAuditLogEntity fromDomain(LedgerAuditLog domain) {
        if (domain == null) return null;

        LedgerAuditLogEntity entity = new LedgerAuditLogEntity();
        entity.id = domain.getId();
        entity.userId = domain.getUserId();
        entity.chargingInvoiceId = domain.getChargingInvoiceId();
        entity.operationType = domain.getOperationType();
        entity.amount = domain.getAmount();
        entity.walletBalanceSnapshot = domain.getWalletBalanceSnapshot();
        entity.historicalPricePerKwh = domain.getHistoricalPricePerKwh();
        entity.historicalTariffName = domain.getHistoricalTariffName();
        entity.deltaKwh = domain.getDeltaKwh();
        entity.deltaMinutes = domain.getDeltaMinutes();
        entity.totalMeterKwh = domain.getTotalMeterKwh();
        entity.auditComment = domain.getAuditComment();
        entity.createdAt = domain.getCreatedAt();
        entity.createdBy = domain.getCreatedBy();
        return entity;
    }

    // --- Маппинг: JPA Entity -> Домен ---

    public LedgerAuditLog toDomain() {
        return new LedgerAuditLog(
                this.id,
                this.userId,
                this.chargingInvoiceId,
                this.operationType,
                this.amount,
                this.walletBalanceSnapshot,
                this.historicalPricePerKwh,
                this.historicalTariffName,
                this.deltaKwh,
                this.deltaMinutes,
                this.totalMeterKwh,
                this.auditComment,
                this.createdAt,
                this.createdBy
        );
    }

    // --- Стандартные геттеры и сеттеры ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getChargingInvoiceId() { return chargingInvoiceId; }
    public void setChargingInvoiceId(Long chargingInvoiceId) { this.chargingInvoiceId = chargingInvoiceId; }

    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getWalletBalanceSnapshot() { return walletBalanceSnapshot; }
    public void setWalletBalanceSnapshot(BigDecimal walletBalanceSnapshot) { this.walletBalanceSnapshot = walletBalanceSnapshot; }

    public BigDecimal getHistoricalPricePerKwh() { return historicalPricePerKwh; }
    public void setHistoricalPricePerKwh(BigDecimal historicalPricePerKwh) { this.historicalPricePerKwh = historicalPricePerKwh; }

    public String getHistoricalTariffName() { return historicalTariffName; }
    public void setHistoricalTariffName(String historicalTariffName) { this.historicalTariffName = historicalTariffName; }

    public BigDecimal getDeltaKwh() { return deltaKwh; }
    public void setDeltaKwh(BigDecimal deltaKwh) { this.deltaKwh = deltaKwh; }

    public Integer getDeltaMinutes() { return deltaMinutes; }
    public void setDeltaMinutes(Integer deltaMinutes) { this.deltaMinutes = deltaMinutes; }

    public BigDecimal getTotalMeterKwh() { return totalMeterKwh; }
    public void setTotalMeterKwh(BigDecimal totalMeterKwh) { this.totalMeterKwh = totalMeterKwh; }

    public String getAuditComment() { return auditComment; }
    public void setAuditComment(String auditComment) { this.auditComment = auditComment; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}

