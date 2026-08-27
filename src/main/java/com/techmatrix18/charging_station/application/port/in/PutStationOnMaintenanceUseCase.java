package com.techmatrix18.charging_station.application.port.in;

import com.techmatrix18.charging_station.application.command.PutStationOnMaintenanceCommand;

/**
 * PutStationOnMaintenanceUseCase
 * Входной порт для перевода зарядной станции на техническое обслуживание
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface PutStationOnMaintenanceUseCase {
    void putStationOnMaintenance(PutStationOnMaintenanceCommand command);
}

