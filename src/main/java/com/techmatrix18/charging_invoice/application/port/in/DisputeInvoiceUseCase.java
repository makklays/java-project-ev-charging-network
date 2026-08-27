package com.techmatrix18.charging_invoice.application.port.in;

import com.techmatrix18.charging_invoice.application.command.DisputeInvoiceCommand;

/**
 * DisputeInvoiceUseCase
 * Входной порт для перевода бухгалтерского документа в режим спора (Disputed)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface DisputeInvoiceUseCase {
    void disputeInvoice(DisputeInvoiceCommand command);
}

