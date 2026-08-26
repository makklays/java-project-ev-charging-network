package com.techmatrix18.user_wallet.infrastructure.http;

import com.techmatrix18.user_wallet.application.command.DebitIdleFeeCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DebitIdleFeeRequest
 * Входящий HTTP-запрос на списание штрафа за оккупацию зарядного кабеля/места
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record DebitIdleFeeRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Session ID is required")
        Long sessionId,

        @NotNull(message = "Debit amount cannot be null")
        @Positive(message = "Idle fee debit amount must be positive")
        BigDecimal amount
) {
    public DebitIdleFeeCommand toCommand() {
        return new DebitIdleFeeCommand(
                this.userId,
                this.sessionId,
                this.amount
        );
    }
}

