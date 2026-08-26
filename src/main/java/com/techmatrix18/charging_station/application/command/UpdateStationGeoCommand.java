package com.techmatrix18.charging_station.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * UpdateStationGeoCommand
 * Корректировка географических координат (latitude, longitude) для точности отображения на карте.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record UpdateStationGeoCommand(
        Long stationId,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public UpdateStationGeoCommand {
        Objects.requireNonNull(stationId, "Station ID is required");
        Objects.requireNonNull(latitude, "Latitude coordinate cannot be null");
        Objects.requireNonNull(longitude, "Longitude coordinate cannot be null");
    }
}

