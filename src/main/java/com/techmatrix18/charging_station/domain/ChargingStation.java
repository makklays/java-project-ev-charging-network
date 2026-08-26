package com.techmatrix18.charging_station.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * ChargingStation
 * Чистая доменная сущность зарядной станции (Локации) сети EV Charging network
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public class ChargingStation {
    private final Long id;
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal maxPowerKw;
    private ChargingStationStatus status;
    private final Long version;
    private final ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    /**
     * Конструктор для первичного создания новой станции (Бизнес-сценарий добавления новой локации)
     */
    public ChargingStation(String name, String address, BigDecimal latitude, BigDecimal longitude, BigDecimal maxPowerKw) {
        this.id = null; // Будет сгенерирован базой данных (BIGSERIAL)
        this.name = Objects.requireNonNull(name, "Station name cannot be null");
        this.address = Objects.requireNonNull(address, "Station address cannot be null");
        this.latitude = Objects.requireNonNull(latitude, "Latitude cannot be null");
        this.longitude = Objects.requireNonNull(longitude, "Longitude cannot be null");
        this.maxPowerKw = Objects.requireNonNull(maxPowerKw, "Max power cannot be null");
        this.status = ChargingStationStatus.ONLINE; // По умолчанию из миграции
        this.version = 0L;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Конструктор для восстановления сущности из базы данных (Маппинг инфраструктуры)
     */
    public ChargingStation(Long id, String name, String address, BigDecimal latitude, BigDecimal longitude,
                           BigDecimal maxPowerKw, ChargingStationStatus status, Long version,
                           ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "Station ID cannot be null");
        this.name = Objects.requireNonNull(name, "Station name cannot be null");
        this.address = Objects.requireNonNull(address, "Station address cannot be null");
        this.latitude = Objects.requireNonNull(latitude, "Latitude cannot be null");
        this.longitude = Objects.requireNonNull(longitude, "Longitude cannot be null");
        this.maxPowerKw = Objects.requireNonNull(maxPowerKw, "Max power cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.version = Objects.requireNonNull(version, "Version cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt date cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "UpdatedAt date cannot be null");
    }

    // --- Бизнес-методы (Rich Domain Model) ---

    /**
     * Перевод станции на техническое обслуживание
     */
    public void startMaintenance() {
        if (this.status == ChargingStationStatus.UNDER_MAINTENANCE) {
            throw new IllegalStateException("Station is already under maintenance");
        }
        this.status = ChargingStationStatus.UNDER_MAINTENANCE;
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Возврат станции в режим работы Online
     */
    public void setOnline() {
        this.status = ChargingStationStatus.ONLINE;
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Фиксация аварийного отключения станции (например, пропала связь с контроллером)
     */
    public void setOffline() {
        this.status = ChargingStationStatus.OFFLINE;
        this.updatedAt = ZonedDateTime.now();
    }

    // --- Геттеры ---
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public BigDecimal getLatitude() { return latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public BigDecimal getMaxPowerKw() { return maxPowerKw; }
    public ChargingStationStatus getStatus() { return status; }
    public Long getVersion() { return version; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
}

