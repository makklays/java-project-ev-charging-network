package com.techmatrix18.evse_point.application.port.out;

import com.techmatrix18.evse_point.domain.EvsePoint;
import java.util.Optional;

/**
 * EvsePointRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface EvsePointRepository {
    EvsePoint save(EvsePoint evsePoint);
    Optional<EvsePoint> findById(Long id);
    boolean existsByStationIdAndEvseNumber(Long stationId, Integer evseNumber);
}

