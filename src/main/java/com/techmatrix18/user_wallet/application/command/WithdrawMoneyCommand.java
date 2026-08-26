package com.techmatrix18.user_wallet.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * WithdrawMoneyCommand
 * Команда на вывод (возврат) денежных средств из кошелька на банковский счет пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 26.08.2026
 */

public record WithdrawMoneyCommand(
        Long userId,
        BigDecimal amount,
        String bankAccountReference
) {
    public WithdrawMoneyCommand {
        Objects.requireNonNull(userId, "User ID is required for money withdrawal");
        Objects.requireNonNull(amount, "Withdrawal amount cannot be null");
        Objects.requireNonNull(bankAccountReference, "Bank account reference is required");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
    }
}

