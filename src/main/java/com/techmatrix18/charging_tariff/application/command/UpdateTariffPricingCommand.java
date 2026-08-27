package com.techmatrix18.charging_tariff.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * UpdateTariffPricingCommand
 * Команда для изменения стоимости за киловатт-часы и время простоя в существующем тарифе
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record UpdateTariffPricingCommand(
        Long tariffId,
        BigDecimal pricePerKwh,
        BigDecimal idlePricePerMin
) {
    public UpdateTariffPricingCommand {
        Objects.requireNonNull(tariffId, "Tariff ID is required");
        Objects.requireNonNull(pricePerKwh, "Price per kWh cannot be null");
        Objects.requireNonNull(idlePricePerMin, "Idle price per minute cannot be null");

        // Финтех-валидация: цены в сети зарядок не могут быть отрицательными
        if (pricePerKwh.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price per kWh cannot be negative");
        }
        if (idlePricePerMin.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Idle price per minute cannot be negative");
        }
    }
}

