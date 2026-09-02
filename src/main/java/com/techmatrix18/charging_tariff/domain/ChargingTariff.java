package com.techmatrix18.charging_tariff.domain;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ChargingTariff
 * Чистая доменная сущность тарифа на зарядку (Time-of-Use Tariff)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public class ChargingTariff {
    private final Long id;
    private final Long connectorId;
    private final String zoneName; // Например: NIGHT, PEAK, STANDARD
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final BigDecimal pricePerKwh;
    private final BigDecimal idlePricePerMin;
    private final Long version;
    private final ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    // Список событий, произошедших с агрегатом в памяти
    private final List<Object> domainEvents = new ArrayList<>();

    /**
     * Конструктор для первичного создания (Бизнес-сценарий заведения нового тарифа администратором)
     */
    public ChargingTariff(Long connectorId, String zoneName, LocalTime startTime, LocalTime endTime,
                          BigDecimal pricePerKwh, BigDecimal idlePricePerMin) {
        this.id = null; // Будет сгенерирован СУБД (BIGSERIAL)
        this.connectorId = Objects.requireNonNull(connectorId, "Connector ID cannot be null");
        this.zoneName = Objects.requireNonNull(zoneName, "Zone name cannot be null");
        this.startTime = Objects.requireNonNull(startTime, "Start time cannot be null");
        this.endTime = Objects.requireNonNull(endTime, "End time cannot be null");
        this.pricePerKwh = Objects.requireNonNull(pricePerKwh, "Price per kWh cannot be null");
        this.idlePricePerMin = Objects.requireNonNull(idlePricePerMin, "Idle price per minute cannot be null");
        this.version = 0L;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();

        validateTariffRules();
    }

    /**
     * Конструктор для восстановления сущности из базы данных (Маппинг инфраструктуры)
     */
    public ChargingTariff(Long id, Long connectorId, String zoneName, LocalTime startTime, LocalTime endTime,
                          BigDecimal pricePerKwh, BigDecimal idlePricePerMin, Long version,
                          ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "Tariff ID cannot be null");
        this.connectorId = Objects.requireNonNull(connectorId, "Connector ID cannot be null");
        this.zoneName = Objects.requireNonNull(zoneName, "Zone name cannot be null");
        this.startTime = Objects.requireNonNull(startTime, "Start time cannot be null");
        this.endTime = Objects.requireNonNull(endTime, "End time cannot be null");
        this.pricePerKwh = Objects.requireNonNull(pricePerKwh, "Price per kWh cannot be null");
        this.idlePricePerMin = Objects.requireNonNull(idlePricePerMin, "Idle price per minute cannot be null");
        this.version = Objects.requireNonNull(version, "Version cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt date cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "UpdatedAt date cannot be null");

        validateTariffRules();
    }

    // --- Инкапсулированные бизнес-методы Rich Domain Model ---

    /**
     * Проверка, действует ли данный тариф в указанное время суток
     * Помогает биллинг-движку определить актуальную стоимость прямо в момент протекания тока
     */
    public boolean isActiveAt(LocalTime timeToCheck) {
        Objects.requireNonNull(timeToCheck, "Time to check cannot be null");

        // Обработка перехода через полночь (например, с 23:00 до 07:00)
        if (startTime.isAfter(endTime)) {
            return !timeToCheck.isBefore(startTime) || timeToCheck.isBefore(endTime);
        }

        // Обычный дневной интервал (например, с 08:00 до 17:00)
        return !timeToCheck.isBefore(startTime) && timeToCheck.isBefore(endTime);
    }

    // --- Внутренняя доменная валидация бизнес-инвариантов ---
    private void validateTariffRules() {
        if (this.zoneName.isBlank()) {
            throw new IllegalArgumentException("Tariff zone name cannot be empty");
        }
        if (this.pricePerKwh.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price per kWh cannot be negative");
        }
        if (this.idlePricePerMin.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Idle price per minute cannot be negative");
        }
        if (this.startTime.equals(this.endTime)) {
            throw new IllegalArgumentException("Tariff start time and end time cannot be equal");
        }
    }

    // Бизнес-метод: Корректировка цен за киловатты и минуты простоя
    public void updatePricing(java.math.BigDecimal pricePerKwh, java.math.BigDecimal idlePricePerMin) {
        this.updatedAt = java.time.ZonedDateTime.now();
        validateTariffRules();
    }

    // Бизнес-метод: Сдвиг временных рамок суточной зоны тарифа
    public void updateTimeBounds(java.time.LocalTime startTime, java.time.LocalTime endTime) {
        this.updatedAt = java.time.ZonedDateTime.now();
        validateTariffRules();
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
    public Long getConnectorId() { return connectorId; }
    public String getZoneName() { return zoneName; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public BigDecimal getPricePerKwh() { return pricePerKwh; }
    public BigDecimal getIdlePricePerMin() { return idlePricePerMin; }
    public Long getVersion() { return version; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
}

