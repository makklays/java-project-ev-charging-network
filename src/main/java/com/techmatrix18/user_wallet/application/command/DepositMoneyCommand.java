package com.techmatrix18.user_wallet.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * DepositMoneyCommand
 * Пополнение баланса пользователем (например, через интеграцию с платежным шлюзом Stripe, LiqPay или Apple Pay)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record DepositMoneyCommand(
        Long userId,
        BigDecimal amount,
        String paymentReference // ID транзакции из платежной системы (Stripe/LiqPay) для аудита
) {
    public DepositMoneyCommand {
        Objects.requireNonNull(userId, "User ID is required");
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(paymentReference, "Payment reference is required");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }
}

