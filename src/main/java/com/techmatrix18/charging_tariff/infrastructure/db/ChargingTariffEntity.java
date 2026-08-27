package com.techmatrix18.charging_tariff.infrastructure.db;

import com.techmatrix18.charging_tariff.domain.ChargingTariff;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 * ChargingTariffEntity
 * JPA Сущность для таблицы "charging_tariffs" (Слой инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Entity
@Table(name = "charging_tariffs")
public class ChargingTariffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "connector_id", nullable = false)
    private Long connectorId;

    @Column(name = "zone_name", nullable = false, length = 100)
    private String zoneName;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "price_per_kwh", nullable = false, precision = 10, scale = 4)
    private BigDecimal pricePerKwh;

    @Column(name = "idle_price_per_min", nullable = false, precision = 10, scale = 4)
    private BigDecimal idlePricePerMin;

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

    @PreUpdate // Метод автоматически вызывается Hibernate перед каждым UPDATE запросом в базу
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    // --- Конструкторы ---
    public ChargingTariffEntity() {
    }

    // --- Маппинг: Из чистого Домена в JPA Entity (Для сохранения в БД) ---
    public static ChargingTariffEntity fromDomain(ChargingTariff domain) {
        if (domain == null) return null;

        ChargingTariffEntity entity = new ChargingTariffEntity();
        entity.id = domain.getId();
        entity.connectorId = domain.getConnectorId();
        entity.zoneName = domain.getZoneName();
        entity.startTime = domain.getStartTime();
        entity.endTime = domain.getEndTime();
        entity.pricePerKwh = domain.getPricePerKwh();
        entity.idlePricePerMin = domain.getIdlePricePerMin();
        entity.version = domain.getVersion(); // Передаем текущую версию для контроля блокировок
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }

    // --- Маппинг: Из JPA Entity в чистый Домен (Для чтения из БД) ---
    public ChargingTariff toDomain() {
        return new ChargingTariff(
                this.id,
                this.connectorId,
                this.zoneName,
                this.startTime,
                this.endTime,
                this.pricePerKwh,
                this.idlePricePerMin,
                this.version, // Возвращаем версию в домен для контроля параллельных запросов
                this.createdAt,
                this.updatedAt
        );
    }

    // --- Стандартные геттеры и сеттеры для JPA ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getConnectorId() { return connectorId; }
    public void setConnectorId(Long connectorId) { this.connectorId = connectorId; }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public BigDecimal getPricePerKwh() { return pricePerKwh; }
    public void setPricePerKwh(BigDecimal pricePerKwh) { this.pricePerKwh = pricePerKwh; }

    public BigDecimal getIdlePricePerMin() { return idlePricePerMin; }
    public void setIdlePricePerMin(BigDecimal idlePricePerMin) { this.idlePricePerMin = idlePricePerMin; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}

