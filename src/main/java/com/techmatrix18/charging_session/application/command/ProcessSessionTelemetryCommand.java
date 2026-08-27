package com.techmatrix18.charging_session.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * ProcessSessionTelemetryCommand
 * Потоковое обновление счетчиков со станции и расчет денег
 * Телеметрическая команда (IoT/Kafka) для потокового обновления счетчиков и цен сессии
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record ProcessSessionTelemetryCommand(
        Long sessionId,
        BigDecimal currentMeterValue,
        BigDecimal currentKwhPrice
) {
    public ProcessSessionTelemetryCommand {
        Objects.requireNonNull(sessionId, "Session ID is required to process telemetry");
        Objects.requireNonNull(currentMeterValue, "Current meter value capacity cannot be null");
        Objects.requireNonNull(currentKwhPrice, "Current kWh price cannot be null");

        // Физическая и финтех валидация
        if (currentMeterValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current meter value cannot be negative physical value");
        }
        if (currentKwhPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current energy price per kWh cannot be negative");
        }
    }
}

