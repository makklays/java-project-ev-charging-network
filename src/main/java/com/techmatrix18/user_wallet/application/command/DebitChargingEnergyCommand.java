package com.techmatrix18.user_wallet.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * DebitChargingEnergyCommand
 * Периодическое списание денег за потребленные киловатт-часы.
 * Вызывается раз в минуту или при каждом получении порции телеметрии от зарядной станции
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record DebitChargingEnergyCommand(
        Long userId,
        Long sessionId,       // К какой сессии зарядки привязано списание
        BigDecimal amount,    // Стоимость за этот конкретный тик
        BigDecimal kwhDelta   // Сколько кВт*ч прокачано за этот период
) {
    public DebitChargingEnergyCommand {
        Objects.requireNonNull(userId, "User ID is required");
        Objects.requireNonNull(sessionId, "Session ID is required");
        Objects.requireNonNull(amount, "Amount cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }
    }
}

