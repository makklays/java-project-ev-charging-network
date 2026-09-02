package com.techmatrix18.charging_station.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    // Список событий, произошедших с агрегатом в памяти
    private final List<Object> domainEvents = new ArrayList<>();

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

    // Перевод станции на техническое обслуживание
    public void startMaintenance() {
        if (this.status == ChargingStationStatus.UNDER_MAINTENANCE) {
            throw new IllegalStateException("Station is already under maintenance");
        }
        this.status = ChargingStationStatus.UNDER_MAINTENANCE;
        this.updatedAt = ZonedDateTime.now();
    }

    // Возврат станции в режим работы Online
    public void setOnline() {
        this.status = ChargingStationStatus.ONLINE;
        this.updatedAt = ZonedDateTime.now();
    }

    // Фиксация аварийного отключения станции (например, пропала связь с контроллером)
    public void setOffline() {
        this.status = ChargingStationStatus.OFFLINE;
        this.updatedAt = ZonedDateTime.now();
    }

    // Бизнес-метод: Корректировка гео-координат расположения зарядной станции
    public void updateGeo(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = java.util.Objects.requireNonNull(latitude, "Latitude coordinate cannot be null");
        this.longitude = java.util.Objects.requireNonNull(longitude, "Longitude coordinate cannot be null");

        // Валидация диапазонов координат Земли для предотвращения грубых ошибок ввода
        if (latitude.compareTo(new BigDecimal("-90")) < 0 || latitude.compareTo(new BigDecimal("90")) > 0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        if (longitude.compareTo(new BigDecimal("-180")) < 0 || longitude.compareTo(new BigDecimal("180")) > 0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }

        this.updatedAt = java.time.ZonedDateTime.now();
    }

    // Бизнес-метод: Динамическое изменение лимита мощности станции
    public void rebalancePower(BigDecimal newMaxPowerKw) {
        java.util.Objects.requireNonNull(newMaxPowerKw, "New maximum power value cannot be null");

        if (newMaxPowerKw.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rebalanced power must be a positive physical value");
        }

        this.maxPowerKw = newMaxPowerKw;
        this.updatedAt = java.time.ZonedDateTime.now();
    }

    // Бизнес-метод: Изменение публичного имени и адреса расположения станции
    public void updateDetails(String name, String address) {
        this.name = java.util.Objects.requireNonNull(name, "Station name cannot be null");
        this.address = java.util.Objects.requireNonNull(address, "Station address cannot be null");

        // Бизнес-валидация: защищаем домен от пустых строк, отправленных из API
        if (name.isBlank()) {
            throw new IllegalArgumentException("Station name cannot be empty or blank");
        }
        if (address.isBlank()) {
            throw new IllegalArgumentException("Station address cannot be empty or blank");
        }

        // Фиксируем точное время изменения метаданных в UTC
        this.updatedAt = java.time.ZonedDateTime.now();
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

