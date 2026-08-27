package com.techmatrix18.connector.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * RegisterConnectorCommand
 * Команда для регистрации и привязки нового физического кабеля к точке EVSE
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record RegisterConnectorCommand(
        Long evseId,
        Integer connectorNumber,
        String connectorType,
        String currentType,
        BigDecimal maxPowerKw
) {
    public RegisterConnectorCommand {
        Objects.requireNonNull(evseId, "EVSE ID is required");
        Objects.requireNonNull(connectorNumber, "Connector number is required");
        Objects.requireNonNull(connectorType, "Connector type is required");
        Objects.requireNonNull(currentType, "Current type is required");
        Objects.requireNonNull(maxPowerKw, "Maximum power value cannot be null");

        // Физическая валидация параметров кабеля
        if (connectorNumber <= 0) {
            throw new IllegalArgumentException("Connector number must be greater than zero");
        }
        if (connectorType.isBlank() || currentType.isBlank()) {
            throw new IllegalArgumentException("Connector type descriptors cannot be blank");
        }
        if (maxPowerKw.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Maximum power capacity must be positive physical value");
        }
    }
}

