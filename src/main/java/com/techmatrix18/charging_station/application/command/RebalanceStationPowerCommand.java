package com.techmatrix18.charging_station.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * RebalanceStationPowerCommand
 * Динамическое изменение лимита общей доступной мощности станции (max_power_kw) по требованию энергосети
 * или системы балансировки нагрузок.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record RebalanceStationPowerCommand(
        Long stationId,
        BigDecimal newMaxPowerKw
) {
    public RebalanceStationPowerCommand {
        Objects.requireNonNull(stationId, "Station ID is required for power rebalancing");
        Objects.requireNonNull(newMaxPowerKw, "New maximum power value cannot be null");

        if (newMaxPowerKw.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rebalanced power must be a positive value");
        }
    }
}

