package com.techmatrix18.evse_point.application.command;

import java.util.Objects;

/**
 * CreateEvsePointCommand
 * Команда для добавления (монтажа) новой точки зарядки EVSE на физическую станцию
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record CreateEvsePointCommand(
        Long stationId,
        Integer evseNumber,
        Integer ocppEvseId
) {
    public CreateEvsePointCommand {
        Objects.requireNonNull(stationId, "Station ID is required");
        Objects.requireNonNull(evseNumber, "EVSE sequence number is required");
        Objects.requireNonNull(ocppEvseId, "OCPP EVSE physical ID is required");

        // Бизнес-проверка физических лимитов оборудования
        if (evseNumber <= 0) {
            throw new IllegalArgumentException("EVSE number must be a positive integer greater than zero");
        }
        if (ocppEvseId < 0) {
            throw new IllegalArgumentException("OCPP EVSE ID cannot be negative");
        }
    }
}

