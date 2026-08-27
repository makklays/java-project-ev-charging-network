package com.techmatrix18.evse_point.domain;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * EvsePoint
 * Чистая доменная сущность зарядной точки (EVSE) по стандарту OCPP 2.0.1
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public class EvsePoint {
    private final Long id;
    private final Long stationId;
    private final Integer evseNumber;
    private EvseStatus status;
    private final Integer ocppEvseId;
    private final Long version;
    private final ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    /**
     * Конструктор для первичного создания новой точки (Сценарий монтажа и расширения станции)
     */
    public EvsePoint(Long stationId, Integer evseNumber, Integer ocppEvseId) {
        this.id = null; // Будет сгенерирован базой данных (BIGSERIAL)
        this.stationId = Objects.requireNonNull(stationId, "Station ID cannot be null");
        this.evseNumber = Objects.requireNonNull(evseNumber, "EVSE number cannot be null");
        this.ocppEvseId = Objects.requireNonNull(ocppEvseId, "OCPP EVSE ID cannot be null");
        this.status = EvseStatus.AVAILABLE; // По умолчанию из миграции
        this.version = 0L;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();

        validateFields();
    }

    /**
     * Конструктор для восстановления сущности из базы данных (Маппинг инфраструктуры)
     */
    public EvsePoint(Long id, Long stationId, Integer evseNumber, EvseStatus status,
                     Integer ocppEvseId, Long version, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "EVSE ID cannot be null");
        this.stationId = Objects.requireNonNull(stationId, "Station ID cannot be null");
        this.evseNumber = Objects.requireNonNull(evseNumber, "EVSE number cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.ocppEvseId = Objects.requireNonNull(ocppEvseId, "OCPP EVSE ID cannot be null");
        this.version = Objects.requireNonNull(version, "Version cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt date cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "UpdatedAt date cannot be null");

        validateFields();
    }

    // --- Инкапсулированные бизнес-методы переключения статусов OCPP ---

    /**
     * Пользователь вставил пистолет в машину, идет подготовка сессии
     */
    public void prepare() {
        this.status = EvseStatus.PREPARING;
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Подача тока успешно запущена, идет зарядка электромобиля
     */
    public void startCharging() {
        this.status = EvseStatus.CHARGING;
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Зарядка приостановлена со стороны автомобиля (например, батарея заполнена до лимита)
     */
    public void suspendByEv() {
        this.status = EvseStatus.SUSPENDED_EV;
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * На плате управления зафиксирован аппаратный сбой (Fault)
     */
    public void logFault() {
        this.status = EvseStatus.FAULTED;
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Освобождение точки, кабель отключен, готова к новым сессиям
     */
    public void release() {
        this.status = EvseStatus.AVAILABLE;
        this.updatedAt = ZonedDateTime.now();
    }

    // --- Внутренняя доменная валидация физических параметров ---
    private void validateFields() {
        if (this.evseNumber <= 0) {
            throw new IllegalArgumentException("EVSE sequence number must be positive and greater than zero");
        }
        if (this.ocppEvseId < 0) {
            throw new IllegalArgumentException("OCPP EVSE ID cannot be negative");
        }
    }

    // --- Геттеры ---
    public Long getId() { return id; }
    public Long getStationId() { return stationId; }
    public Integer getEvseNumber() { return evseNumber; }
    public EvseStatus getStatus() { return status; }
    public Integer getOcppEvseId() { return ocppEvseId; }
    public Long getVersion() { return version; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
}

