package com.techmatrix18.charging_invoice.application.command;

import java.util.Objects;

/**
 * PayInvoiceCommand
 * Фиксация успешного списания денег и закрытия долга
 * Команда для фиксации успешного проведения платежа и закрытия счета (PAID)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record PayInvoiceCommand(
        Long invoiceId
) {
    public PayInvoiceCommand {
        Objects.requireNonNull(invoiceId, "Invoice ID is required to mark the invoice as paid");
    }
}

