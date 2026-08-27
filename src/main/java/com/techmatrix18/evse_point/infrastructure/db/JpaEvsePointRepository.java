package com.techmatrix18.evse_point.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JpaEvsePointRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Repository
public interface JpaEvsePointRepository extends JpaRepository<EvsePointEntity, Long> {

    // Проверка уникальности комбинации station_id и evse_number (из ограничения UNIQUE)
    boolean existsByStationIdAndEvseNumber(Long stationId, Integer evseNumber);
}

