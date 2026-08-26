package com.techmatrix18.charging_station.application.port.out;

import com.techmatrix18.charging_station.domain.ChargingStation;
import java.util.Optional;

/**
 * ChargingStationRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface ChargingStationRepository {
    ChargingStation save(ChargingStation station);
    Optional<ChargingStation> findById(Long id);
}

