package com.techmatrix18.charging_station.application.port.in;

import com.techmatrix18.charging_station.application.command.UpdateStationDetailsCommand;

/**
 * UpdateStationDetailsUseCase
 * Входной порт для обновления текстовых данных зарядной станции
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface UpdateStationDetailsUseCase {
    void updateStationDetails(UpdateStationDetailsCommand command);
}

