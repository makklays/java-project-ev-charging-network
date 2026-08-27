package com.techmatrix18.charging_invoice.application.command;

import java.util.Objects;

/**
 * MarkInvoiceAsFailedCommand
 * Фиксация овердрафта или ошибки эквайринга
 * Команда для фиксации ошибки оплаты счета (например, из-за нехватки средств на кошельке или блокировки карты)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record MarkInvoiceAsFailedCommand(
        Long invoiceId
) {
    public MarkInvoiceAsFailedCommand {
        Objects.requireNonNull(invoiceId, "Invoice ID is required to mark invoice as failed");
    }
}

