package com.techmatrix18.charging_station.infrastructure.db;

import com.techmatrix18.charging_station.domain.ChargingStation;
import com.techmatrix18.charging_station.domain.ChargingStationStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * ChargingStationEntity
 * JPA Сущность для таблицы "charging_stations" (Слой инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Entity
@Table(name = "charging_stations")
public class ChargingStationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "max_power_kw", nullable = false, precision = 6, scale = 2)
    private BigDecimal maxPowerKw;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ChargingStationStatus status;

    @Version // Активирует Optimistic Locking на основе вашей миграции
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // --- Управление временными метками жизненного цикла JPA ---
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = ZonedDateTime.now();
        }
        this.updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    // --- Конструкторы ---
    public ChargingStationEntity() {
    }

    // --- Маппинг: Домен -> JPA Entity ---
    public static ChargingStationEntity fromDomain(ChargingStation station) {
        if (station == null) return null;

        ChargingStationEntity entity = new ChargingStationEntity();
        entity.id = station.getId();
        entity.name = station.getName();
        entity.address = station.getAddress();
        entity.latitude = station.getLatitude();
        entity.longitude = station.getLongitude();
        entity.maxPowerKw = station.getMaxPowerKw();
        entity.status = station.getStatus();
        entity.version = station.getVersion();
        entity.createdAt = station.getCreatedAt();
        entity.updatedAt = station.getUpdatedAt();
        return entity;
    }

    // --- Маппинг: JPA Entity -> Домен ---
    public ChargingStation toDomain() {
        return new ChargingStation(
                this.id,
                this.name,
                this.address,
                this.latitude,
                this.longitude,
                this.maxPowerKw,
                this.status,
                this.version,
                this.createdAt,
                this.updatedAt
        );
    }

    // --- Стандартные геттеры и сеттеры ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public BigDecimal getMaxPowerKw() { return maxPowerKw; }
    public void setMaxPowerKw(BigDecimal maxPowerKw) { this.maxPowerKw = maxPowerKw; }

    public ChargingStationStatus getStatus() { return status; }
    public void setStatus(ChargingStationStatus status) { this.status = status; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}

