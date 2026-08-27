package com.techmatrix18.charging_invoice.application.port.in;

import com.techmatrix18.charging_invoice.application.command.PayInvoiceCommand;

/**
 * PayInvoiceUseCase
 * Входной порт для подтверждения успешной оплаты выставленного инвойса
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface PayInvoiceUseCase {
    void payInvoice(PayInvoiceCommand command);
}

