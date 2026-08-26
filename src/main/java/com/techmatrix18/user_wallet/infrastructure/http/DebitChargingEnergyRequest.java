package com.techmatrix18.user_wallet.infrastructure.http;

import com.techmatrix18.user_wallet.application.command.DebitChargingEnergyCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DebitChargingEnergyRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record DebitChargingEnergyRequest(
        @NotNull(message = "User ID is required") Long userId,
        @NotNull(message = "Session ID is required") Long sessionId,
        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Debit amount must be positive") BigDecimal amount,
        @NotNull(message = "kWh delta cannot be null") BigDecimal kwhDelta
) {
    public DebitChargingEnergyCommand toCommand() {
        return new DebitChargingEnergyCommand(this.userId, this.sessionId, this.amount, this.kwhDelta);
    }
}

