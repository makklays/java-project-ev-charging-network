package com.techmatrix18.charging_session.infrastructure.db;

import com.techmatrix18.charging_session.domain.ChargingSession;
import com.techmatrix18.charging_session.domain.ChargingSessionStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * ChargingSessionEntity
 * JPA Сущность для таблицы "charging_sessions" (Слой инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Entity
@Table(name = "charging_sessions")
public class ChargingSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "connector_id", nullable = false)
    private Long connectorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ChargingSessionStatus status;

    @Column(name = "started_at", nullable = false)
    private ZonedDateTime startedAt;

    @Column(name = "finished_at")
    private ZonedDateTime finishedAt;

    @Column(name = "start_meter_value", nullable = false, precision = 12, scale = 3)
    private BigDecimal startMeterValue;

    @Column(name = "last_meter_value", nullable = false, precision = 12, scale = 3)
    private BigDecimal lastMeterValue;

    @Column(name = "total_kwh_consumed", nullable = false, precision = 12, scale = 3)
    private BigDecimal totalKwhConsumed;

    @Column(name = "total_energy_amount", nullable = false, precision = 12, scale = 4)
    private BigDecimal totalEnergyAmount;

    @Column(name = "total_idle_amount", nullable = false, precision = 12, scale = 4)
    private BigDecimal totalIdleAmount;

    @Column(name = "total_final_amount", nullable = false, precision = 12, scale = 4)
    private BigDecimal totalFinalAmount;

    @Version // Активирует Optimistic Locking на основе поля version BIGINT из вашей миграции
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // --- Автоматическое управление временными метками жизненного цикла JPA ---
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = ZonedDateTime.now();
        }
        this.updatedAt = ZonedDateTime.now();
    }

    @PreUpdate // Вызывается Hibernate автоматически перед отправкой каждого UPDATE-запроса в БД
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    // --- Конструкторы ---
    public ChargingSessionEntity() {
    }

    // --- Маппинг: Из чистого Домена в JPA Entity (Для сохранения в БД) ---
    public static ChargingSessionEntity fromDomain(ChargingSession domain) {
        if (domain == null) return null;

        ChargingSessionEntity entity = new ChargingSessionEntity();
        entity.id = domain.getId();
        entity.userId = domain.getUserId();
        entity.connectorId = domain.getConnectorId();
        entity.status = domain.getStatus();
        entity.startedAt = domain.getStartedAt();
        entity.finishedAt = domain.getFinishedAt();
        entity.startMeterValue = domain.getStartMeterValue();
        entity.lastMeterValue = domain.getLastMeterValue();
        entity.totalKwhConsumed = domain.getTotalKwhConsumed();
        entity.totalEnergyAmount = domain.getTotalEnergyAmount();
        entity.totalIdleAmount = domain.getTotalIdleAmount();
        entity.totalFinalAmount = domain.getTotalFinalAmount();
        entity.version = domain.getVersion(); // Передаем текущую версию для контроля блокировок
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }

    // --- Маппинг: Из JPA Entity в чистый Домен (Для чтения из БД) ---
    public ChargingSession toDomain() {
        return new ChargingSession(
                this.id,
                this.userId,
                this.connectorId,
                this.status,
                this.startedAt,
                this.finishedAt,
                this.startMeterValue,
                this.lastMeterValue,
                this.totalKwhConsumed,
                this.totalEnergyAmount,
                this.totalIdleAmount,
                this.totalFinalAmount,
                this.version, // Передаем техническую версию обратно в доменное ядро
                this.createdAt,
                this.updatedAt
        );
    }

    // --- Стандартные геттеры и сеттеры для JPA ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getConnectorId() { return connectorId; }
    public void setConnectorId(Long connectorId) { this.connectorId = connectorId; }

    public ChargingSessionStatus getStatus() { return status; }
    public void setStatus(ChargingSessionStatus status) { this.status = status; }

    public ZonedDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(ZonedDateTime startedAt) { this.startedAt = startedAt; }

    public ZonedDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(ZonedDateTime finishedAt) { this.finishedAt = finishedAt; }

    public BigDecimal getStartMeterValue() { return startMeterValue; }
    public void setStartMeterValue(BigDecimal startMeterValue) { this.startMeterValue = startMeterValue; }

    public BigDecimal getLastMeterValue() { return lastMeterValue; }
    public void setLastMeterValue(BigDecimal lastMeterValue) { this.lastMeterValue = lastMeterValue; }

    public BigDecimal getTotalKwhConsumed() { return totalKwhConsumed; }
    public void setTotalKwhConsumed(BigDecimal totalKwhConsumed) { this.totalKwhConsumed = totalKwhConsumed; }

    public BigDecimal getTotalEnergyAmount() { return totalEnergyAmount; }
    public void setTotalEnergyAmount(BigDecimal totalEnergyAmount) { this.totalEnergyAmount = totalEnergyAmount; }

    public BigDecimal getTotalIdleAmount() { return totalIdleAmount; }
    public void setTotalIdleAmount(BigDecimal totalIdleAmount) { this.totalIdleAmount = totalIdleAmount; }

    public BigDecimal getTotalFinalAmount() { return totalFinalAmount; }
    public void setTotalFinalAmount(BigDecimal totalFinalAmount) { this.totalFinalAmount = totalFinalAmount; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}

