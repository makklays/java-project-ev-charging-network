package com.techmatrix18.charging_tariff.application.port.out;

import com.techmatrix18.charging_tariff.domain.ChargingTariff;
import java.util.Optional;

/**
 * ChargingTariffRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface ChargingTariffRepository {
    ChargingTariff save(ChargingTariff tariff);
    Optional<ChargingTariff> findById(Long id);
    void deleteById(Long id);
}

