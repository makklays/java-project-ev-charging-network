package com.techmatrix18.charging_station.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * RegisterStationCommand
 * Команда для добавления (регистрации) новой зарядной станции в систему
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record RegisterStationCommand(
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal maxPowerKw
) {
    public RegisterStationCommand {
        Objects.requireNonNull(name, "Station name is required");
        if (name.isBlank()) throw new IllegalArgumentException("Station name cannot be empty");

        Objects.requireNonNull(address, "Station address is required");
        if (address.isBlank()) throw new IllegalArgumentException("Station address cannot be empty");

        Objects.requireNonNull(latitude, "Latitude coordinate cannot be null");
        Objects.requireNonNull(longitude, "Longitude coordinate cannot be null");
        Objects.requireNonNull(maxPowerKw, "Maximum power value cannot be null");

        // Базовая математическая проверка физических величин
        if (maxPowerKw.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Maximum power must be a positive value");
        }
    }
}

