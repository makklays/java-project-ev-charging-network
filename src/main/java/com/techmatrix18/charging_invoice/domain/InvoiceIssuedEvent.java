package com.techmatrix18.charging_invoice.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * InvoiceIssuedEvent
 * Доменное событие генерации и закрытия счета с расчетом НДС для клиента.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class InvoiceIssuedEvent {
    private final Long invoiceId;
    private final Long userId;
    private final BigDecimal finalAmount;
    private final ZonedDateTime timestamp;

    public InvoiceIssuedEvent(Long invoiceId, Long userId, BigDecimal finalAmount) {
        this.invoiceId = invoiceId;
        this.userId = userId;
        this.finalAmount = finalAmount;
        this.timestamp = ZonedDateTime.now();
    }

    public Long getInvoiceId() { return invoiceId; }
    public Long getUserId() { return userId; }
    public BigDecimal getFinalAmount() { return finalAmount; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}

