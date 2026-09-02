package com.techmatrix18.charging_invoice.domain;

import java.time.ZonedDateTime;

/**
 * InvoicePaidEvent
 * Доменное событие успешного прохождения эквайринга и перевода инвойса в статус PAID.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class InvoicePaidEvent {
    private final Long invoiceId;
    private final String invoiceNumber;
    private final ZonedDateTime timestamp;

    public InvoicePaidEvent(Long invoiceId, String invoiceNumber) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.timestamp = ZonedDateTime.now();
    }

    public Long getInvoiceId() { return invoiceId; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}

