package com.techmatrix18.connector.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Connector
 * Чистая доменная сущность физического зарядного коннектора (кабеля/пистолета)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public class Connector {
    private final Long id;
    private final Long evseId;
    private final Integer connectorNumber;
    private String connectorType; // Например: CCS2, CHADEMO, TYPE2
    private final String currentType;   // AC или DC
    private BigDecimal maxPowerKw;
    private ConnectorStatus status;
    private final Long version;
    private final ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    // Список событий, произошедших с агрегатом в памяти
    private final List<Object> domainEvents = new ArrayList<>();

    /**
     * Конструктор для первичного создания (Бизнес-сценарий монтажа нового кабеля)
     */
    public Connector(Long evseId, Integer connectorNumber, String connectorType, String currentType, BigDecimal maxPowerKw) {
        this.id = null; // БД (BIGSERIAL)
        this.evseId = Objects.requireNonNull(evseId, "EVSE ID cannot be null");
        this.connectorNumber = Objects.requireNonNull(connectorNumber, "Connector number cannot be null");
        this.connectorType = Objects.requireNonNull(connectorType, "Connector type cannot be null");
        this.currentType = Objects.requireNonNull(currentType, "Current type cannot be null");
        this.maxPowerKw = Objects.requireNonNull(maxPowerKw, "Max power cannot be null");
        this.status = ConnectorStatus.AVAILABLE; // По умолчанию из миграции
        this.version = 0L;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();

        validatePhysicalParameters();
    }

    /**
     * Конструктор для восстановления сущности из базы данных (Маппинг инфраструктуры)
     */
    public Connector(Long id, Long evseId, Integer connectorNumber, String connectorType, String currentType,
                     BigDecimal maxPowerKw, ConnectorStatus status, Long version,
                     ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "Connector ID cannot be null");
        this.evseId = Objects.requireNonNull(evseId, "EVSE ID cannot be null");
        this.connectorNumber = Objects.requireNonNull(connectorNumber, "Connector number cannot be null");
        this.connectorType = Objects.requireNonNull(connectorType, "Connector type cannot be null");
        this.currentType = Objects.requireNonNull(currentType, "Current type cannot be null");
        this.maxPowerKw = Objects.requireNonNull(maxPowerKw, "Max power cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.version = Objects.requireNonNull(version, "Version cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt date cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "UpdatedAt date cannot be null");

        validatePhysicalParameters();
    }

    // --- Инкапсулированные бизнес-методы (Rich Domain Model) ---

    /**
     * Запуск процесса зарядки через данный кабель
     */
    public void occupy() {
        if (this.status == ConnectorStatus.CHARGING) {
            throw new IllegalStateException("Connector is already in use and charging");
        }
        if (this.status == ConnectorStatus.FAULTED) {
            throw new IllegalStateException("Cannot start charging: Connector is broken/faulted");
        }
        this.status = ConnectorStatus.CHARGING;
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Фиксация аппаратной поломки пистолета (например, сломался замок блокиратора CCS2 или перегрелся датчик)
     */
    public void reportFault() {
        this.status = ConnectorStatus.FAULTED;
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Успешное завершение зарядки, кабель возвращен на станцию
     */
    public void makeAvailable() {
        this.status = ConnectorStatus.AVAILABLE;
        this.updatedAt = ZonedDateTime.now();
    }

    // --- Внутренняя доменная валидация физических параметров ---
    private void validatePhysicalParameters() {
        if (this.connectorNumber <= 0) {
            throw new IllegalArgumentException("Connector number must be greater than zero");
        }
        if (this.connectorType.isBlank() || this.currentType.isBlank()) {
            throw new IllegalArgumentException("Connector type descriptors cannot be blank");
        }
        if (this.maxPowerKw.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Maximum power capacity must be positive physical value");
        }
    }

    // Бизнес-метод: Модернизация физических характеристик зарядного пистолета
    public void updateSpecs(String connectorType, java.math.BigDecimal maxPowerKw) {
        this.connectorType = java.util.Objects.requireNonNull(connectorType, "Connector type cannot be null");
        this.maxPowerKw = java.util.Objects.requireNonNull(maxPowerKw, "Maximum power value cannot be null");

        if (connectorType.isBlank()) {
            throw new IllegalArgumentException("Connector type description cannot be empty or blank");
        }
        if (maxPowerKw.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Connector maximum power capacity must be a positive physical value");
        }

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

    // --- Геттеры для портов и мапперов ---

    public Long getId() { return id; }
    public Long getEvseId() { return evseId; }
    public Integer getConnectorNumber() { return connectorNumber; }
    public String getConnectorType() { return connectorType; }
    public String getCurrentType() { return currentType; }
    public BigDecimal getMaxPowerKw() { return maxPowerKw; }
    public ConnectorStatus getStatus() { return status; }
    public Long getVersion() { return version; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
}

