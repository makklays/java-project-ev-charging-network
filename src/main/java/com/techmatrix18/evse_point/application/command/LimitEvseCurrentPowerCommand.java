package com.techmatrix18.evse_point.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * LimitEvseCurrentPowerCommand
 * Команда алгоритма балансировки (Smart Charging) для динамического ограничения мощности точки зарядки
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record LimitEvseCurrentPowerCommand(
        Long evseId,
        BigDecimal maxPowerKw,
        Integer maxCurrentAmps // Сила тока важна для OCPP профилей зарядки (ChargingProfile)
) {
    public LimitEvseCurrentPowerCommand {
        Objects.requireNonNull(evseId, "EVSE ID is required");
        Objects.requireNonNull(maxPowerKw, "Max power limit capacity cannot be null");
        Objects.requireNonNull(maxCurrentAmps, "Max current Amperes limit cannot be null");

        // Физическая валидация параметров электросети
        if (maxPowerKw.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Power limit cannot be negative");
        }
        if (maxCurrentAmps < 0) {
            throw new IllegalArgumentException("Current Amperes limit cannot be negative");
        }
        // Например, минимальный рабочий ток по стандарту IEC 61851 для старта зарядки — 6 Ампер
        if (maxCurrentAmps > 0 && maxCurrentAmps < 6) {
            throw new IllegalArgumentException("Minimum current limit for EV charging must be at least 6 Amps");
        }
    }
}

