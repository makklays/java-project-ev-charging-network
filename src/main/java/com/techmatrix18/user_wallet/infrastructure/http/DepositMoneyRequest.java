package com.techmatrix18.user_wallet.infrastructure.http;

import com.techmatrix18.user_wallet.application.command.DepositMoneyCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DepositMoneyRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record DepositMoneyRequest(
        @NotNull(message = "User ID is required") Long userId,
        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Deposit amount must be positive") BigDecimal amount,
        @NotBlank(message = "Payment reference is required") String paymentReference
) {
    public DepositMoneyCommand toCommand() {
        return new DepositMoneyCommand(this.userId, this.amount, this.paymentReference);
    }
}

