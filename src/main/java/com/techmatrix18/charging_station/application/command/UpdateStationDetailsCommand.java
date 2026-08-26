package com.techmatrix18.charging_station.application.command;

import java.util.Objects;

/**
 * UpdateStationDetailsCommand
 * Изменение публичных данных станции (переименование, обновление адреса).
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record UpdateStationDetailsCommand(
        Long stationId,
        String name,
        String address
) {
    public UpdateStationDetailsCommand {
        Objects.requireNonNull(stationId, "Station ID is required");
        Objects.requireNonNull(name, "Station name cannot be null");
        if (name.isBlank()) throw new IllegalArgumentException("Station name cannot be empty");

        Objects.requireNonNull(address, "Station address cannot be null");
        if (address.isBlank()) throw new IllegalArgumentException("Station address cannot be empty");
    }
}

