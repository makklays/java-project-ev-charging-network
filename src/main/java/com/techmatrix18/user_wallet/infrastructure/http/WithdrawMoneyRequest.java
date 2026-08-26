package com.techmatrix18.user_wallet.infrastructure.http;

import com.techmatrix18.user_wallet.application.command.WithdrawMoneyCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * WithdrawMoneyRequest
 * Входящий HTTP-запрос на вывод (возврат) денежных средств из кошелька на карту
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record WithdrawMoneyRequest(
        @NotNull(message = "User ID is required for withdrawal")
        Long userId,

        @NotNull(message = "Withdrawal amount cannot be null")
        @Positive(message = "Withdrawal amount must be positive")
        BigDecimal amount,

        @NotBlank(message = "Bank account reference cannot be empty")
        String bankAccountReference
) {
    public WithdrawMoneyCommand toCommand() {
        return new WithdrawMoneyCommand(
                this.userId,
                this.amount,
                this.bankAccountReference
        );
    }
}

