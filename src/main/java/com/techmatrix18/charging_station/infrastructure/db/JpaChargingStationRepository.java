package com.techmatrix18.charging_station.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JpaChargingStationRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Repository
public interface JpaChargingStationRepository extends JpaRepository<ChargingStationEntity, Long> {

}

