package com.techmatrix18.charging_station.application.port.in;

import com.techmatrix18.charging_station.application.command.SetStationOnlineCommand;

/**
 * SetStationOnlineUseCase
 * Входной порт для перевода зарядной станции в рабочий режим (Online)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface SetStationOnlineUseCase {
    void setStationOnline(SetStationOnlineCommand command);
}

