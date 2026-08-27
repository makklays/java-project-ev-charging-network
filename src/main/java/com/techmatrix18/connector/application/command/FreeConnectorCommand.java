package com.techmatrix18.connector.application.command;

import java.util.Objects;

/**
 * FreeConnectorCommand
 * Телеметрическая команда (IoT/Kafka), фиксирующая возврат пистолета на станцию и освобождение кабеля
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record FreeConnectorCommand(
        Long connectorId
) {
    public FreeConnectorCommand {
        Objects.requireNonNull(connectorId, "Connector ID is required to free the charging cable");
    }
}

