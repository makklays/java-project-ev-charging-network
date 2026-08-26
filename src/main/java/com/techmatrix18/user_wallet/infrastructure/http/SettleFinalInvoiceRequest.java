package com.techmatrix18.user_wallet.infrastructure.http;

import com.techmatrix18.user_wallet.application.command.SettleFinalInvoiceCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 * SettleFinalInvoiceRequest
 * Входящий HTTP-запрос на полное закрытие бухгалтерского инвойса зарядной сессии
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record SettleFinalInvoiceRequest(
        @NotNull(message = "User ID is required for invoice settlement")
        Long userId,

        @NotNull(message = "Invoice ID is required for invoice settlement")
        Long invoiceId,

        @NotNull(message = "Final amount cannot be null")
        @PositiveOrZero(message = "Final amount must be positive or zero")
        BigDecimal finalAmount
) {
    public SettleFinalInvoiceCommand toCommand() {
        return new SettleFinalInvoiceCommand(
                this.userId,
                this.invoiceId,
                this.finalAmount
        );
    }
}

