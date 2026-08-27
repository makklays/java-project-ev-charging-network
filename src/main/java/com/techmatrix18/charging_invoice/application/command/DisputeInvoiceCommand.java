package com.techmatrix18.charging_invoice.application.command;

import java.util.Objects;

/**
 * DisputeInvoiceCommand
 * Заморозка инвойса на период разбора жалобы службой поддержки
 * Команда для перевода счета в статус оспаривания (DISPUTED) при возникновении жалоб от клиента
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record DisputeInvoiceCommand(
        Long invoiceId
) {
    public DisputeInvoiceCommand {
        Objects.requireNonNull(invoiceId, "Invoice ID is required to initiate a dispute process");
    }
}

