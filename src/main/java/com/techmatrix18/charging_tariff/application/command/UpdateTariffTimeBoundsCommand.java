package com.techmatrix18.charging_tariff.application.command;

import java.time.LocalTime;
import java.util.Objects;

/**
 * UpdateTariffTimeBoundsCommand
 * Команда для изменения временных рамок суточной тарифной зоны (Time-of-Use bounds)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record UpdateTariffTimeBoundsCommand(
        Long tariffId,
        LocalTime startTime,
        LocalTime endTime
) {
    public UpdateTariffTimeBoundsCommand {
        Objects.requireNonNull(tariffId, "Tariff ID is required");
        Objects.requireNonNull(startTime, "Start time cannot be null");
        Objects.requireNonNull(endTime, "End time cannot be null");

        // Бизнес-валидация: тарифная зона не может иметь нулевую длительность
        if (startTime.equals(endTime)) {
            throw new IllegalArgumentException("Tariff start time and end time cannot be identical");
        }
    }
}

