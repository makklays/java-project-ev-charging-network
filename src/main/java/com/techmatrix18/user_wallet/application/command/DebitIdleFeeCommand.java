package com.techmatrix18.user_wallet.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * DebitIdleFeeCommand
 * Команда для списания штрафа за простой (оккупацию кабеля после 100% зарядки)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 26.08.2026
 */

public record DebitIdleFeeCommand(
        Long userId,
        Long sessionId,
        BigDecimal amount
) {
    public DebitIdleFeeCommand {
        Objects.requireNonNull(userId, "User ID is required for idle fee debit");
        Objects.requireNonNull(sessionId, "Session ID is required for idle fee debit");
        Objects.requireNonNull(amount, "Debit amount cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Idle fee debit amount must be positive");
        }
    }
}

