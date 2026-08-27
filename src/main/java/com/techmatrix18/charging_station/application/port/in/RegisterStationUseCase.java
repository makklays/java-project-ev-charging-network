package com.techmatrix18.charging_station.application.port.in;

import com.techmatrix18.charging_station.application.command.RegisterStationCommand;

/**
 * RegisterStationUseCase
 * Входной порт для добавления новой зарядной станции в сеть EV Charging network
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface RegisterStationUseCase {
    void registerStation(RegisterStationCommand command);
}

