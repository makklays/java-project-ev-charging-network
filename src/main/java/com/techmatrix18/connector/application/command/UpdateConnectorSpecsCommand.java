package com.techmatrix18.connector.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * UpdateConnectorSpecsCommand
 * Команда для модернизации и обновления технических характеристик физического кабеля
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record UpdateConnectorSpecsCommand(
        Long connectorId,
        String connectorType,
        BigDecimal maxPowerKw
) {
    public UpdateConnectorSpecsCommand {
        Objects.requireNonNull(connectorId, "Connector ID is required");
        Objects.requireNonNull(connectorType, "Connector type is required");
        Objects.requireNonNull(maxPowerKw, "Maximum power value cannot be null");

        if (connectorType.isBlank()) {
            throw new IllegalArgumentException("Connector type description cannot be empty");
        }
        if (maxPowerKw.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Maximum power capacity must be a positive physical value");
        }
    }
}

