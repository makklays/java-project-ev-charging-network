package com.techmatrix18.charging_station.application.port.in;

import com.techmatrix18.charging_station.application.command.UpdateStationGeoCommand;

/**
 * UpdateStationGeoUseCase
 * Входной порт для обновления географических координат зарядной станции
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface UpdateStationGeoUseCase {
    void updateStationGeo(UpdateStationGeoCommand command);
}

