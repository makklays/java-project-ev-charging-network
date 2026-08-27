package com.techmatrix18.charging_station.application.port.in;

import com.techmatrix18.charging_station.application.command.SetStationOfflineCommand;

/**
 * SetStationOfflineUseCase
 * Входной порт для перевода зарядной станции в автономный режим (Offline) при потере связи
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface SetStationOfflineUseCase {
    void setStationOffline(SetStationOfflineCommand command);
}

