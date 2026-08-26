package com.techmatrix18.user_wallet.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * SettleFinalInvoiceCommand
 * Команда для полного финального расчета по инвойсу при завершении зарядной сессии
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 26.08.2026
 */

public record SettleFinalInvoiceCommand(
        Long userId,
        Long invoiceId,
        BigDecimal finalAmount
) {
    public SettleFinalInvoiceCommand {
        Objects.requireNonNull(userId, "User ID is required for invoice settlement");
        Objects.requireNonNull(invoiceId, "Invoice ID is required for invoice settlement");
        Objects.requireNonNull(finalAmount, "Final amount cannot be null");

        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Final settlement amount cannot be negative");
        }
    }
}

