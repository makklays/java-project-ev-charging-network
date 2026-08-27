package com.techmatrix18.charging_tariff.application.command;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Objects;

/**
 * CreateTariffCommand
 * Команда для создания новой тарифной зоны для коннектора
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record CreateTariffCommand(
        Long connectorId,
        String zoneName,
        LocalTime startTime,
        LocalTime endTime,
        BigDecimal pricePerKwh,
        BigDecimal idlePricePerMin
) {
    public CreateTariffCommand {
        Objects.requireNonNull(connectorId, "Connector ID is required");
        Objects.requireNonNull(zoneName, "Tariff zone name is required");
        Objects.requireNonNull(startTime, "Start time is required");
        Objects.requireNonNull(endTime, "End time is required");
        Objects.requireNonNull(pricePerKwh, "Price per kWh cannot be null");
        Objects.requireNonNull(idlePricePerMin, "Idle price per minute cannot be null");

        // Финансовая и физическая валидация параметров тарифа
        if (zoneName.isBlank()) {
            throw new IllegalArgumentException("Zone name cannot be empty");
        }
        if (pricePerKwh.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price per kWh cannot be negative");
        }
        if (idlePricePerMin.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Idle price per minute cannot be negative");
        }
        if (startTime.equals(endTime)) {
            throw new IllegalArgumentException("Start time and end time cannot be equal");
        }
    }
}

