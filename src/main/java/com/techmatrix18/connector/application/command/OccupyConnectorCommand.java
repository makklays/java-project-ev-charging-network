package com.techmatrix18.connector.application.command;

import java.util.Objects;

/**
 * OccupyConnectorCommand
 * Телеметрическая команда (IoT/Kafka), фиксирующая замыкание реле и старт протекания тока через кабель
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record OccupyConnectorCommand(
        Long connectorId
) {
    public OccupyConnectorCommand {
        Objects.requireNonNull(connectorId, "Connector ID is required to occupy the charging cable");
    }
}

