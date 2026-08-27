package com.techmatrix18.connector.application.command;

import java.util.Objects;

/**
 * ReportConnectorFaultCommand
 * Телеметрическая команда (IoT/Kafka), фиксирующая аппаратный сбой или повреждение зарядного кабеля
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record ReportConnectorFaultCommand(
        Long connectorId,
        String errorReason
) {
    public ReportConnectorFaultCommand {
        Objects.requireNonNull(connectorId, "Connector ID is required to log a fault");
        Objects.requireNonNull(errorReason, "Error reason descriptor cannot be null");

        if (errorReason.isBlank()) {
            throw new IllegalArgumentException("Error reason description cannot be empty");
        }
    }
}

