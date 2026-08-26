package com.techmatrix18.charging_station.application.command;

import java.util.Objects;

/**
 * PutStationOnMaintenanceCommand
 * Перевод локации в режим технического обслуживания сервисной службой
 * (блокирует возможность старта новых зарядных сессий для водителей).
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record PutStationOnMaintenanceCommand(
        Long stationId
) {
    public PutStationOnMaintenanceCommand {
        Objects.requireNonNull(stationId, "Station ID is required for maintenance");
    }
}

