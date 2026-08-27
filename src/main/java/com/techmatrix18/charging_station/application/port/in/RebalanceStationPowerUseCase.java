package com.techmatrix18.charging_station.application.port.in;

import com.techmatrix18.charging_station.application.command.RebalanceStationPowerCommand;

/**
 * RebalanceStationPowerUseCase
 * Входной порт для динамического изменения лимита общей мощности зарядной станции
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface RebalanceStationPowerUseCase {
    void rebalanceStationPower(RebalanceStationPowerCommand command);
}

