package com.techmatrix18.charging_station.application.command;

import java.util.Objects;

/**
 * SetStationOfflineCommand
 * Фиксация потери связи со станцией (генерируется IoT-модулем, если от станции не поступают телеметрические
 * данные в течение определенного времени).
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record SetStationOfflineCommand(
        Long stationId
) {
    public SetStationOfflineCommand {
        Objects.requireNonNull(stationId, "Station ID is required to set offline");
    }
}

