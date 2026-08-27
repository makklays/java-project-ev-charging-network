package com.techmatrix18.charging_session.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * StartChargingSessionCommand
 * Инициализация и старт процесса зарядки водителем
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record StartChargingSessionCommand(
        Long userId,
        Long connectorId,
        BigDecimal startMeterValue
) {
    public StartChargingSessionCommand {
        Objects.requireNonNull(userId, "User ID is required to start a session");
        Objects.requireNonNull(connectorId, "Connector ID is required to start a session");
        Objects.requireNonNull(startMeterValue, "Initial start meter value cannot be null");

        // Физическая валидация счетчика заправки
        if (startMeterValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial meter value cannot be a negative physical value");
        }
    }
}

