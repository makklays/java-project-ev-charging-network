package com.techmatrix18.charging_station.application.command;

import java.util.Objects;

/**
 * SetStationOnlineCommand
 * Принудительный перевод станции в рабочий режим (вручную диспетчером или автоматически при успешном ответе
 * на пинг (Heartbeat) от IoT-контроллера).
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record SetStationOnlineCommand(
        Long stationId
) {
    public SetStationOnlineCommand {
        Objects.requireNonNull(stationId, "Station ID is required to set online");
    }
}

