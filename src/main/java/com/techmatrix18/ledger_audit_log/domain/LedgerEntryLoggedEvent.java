package com.techmatrix18.ledger_audit_log.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * LedgerEntryLoggedEvent
 * Доменное событие фиксации новой стандартной Append-Only проводки.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class LedgerEntryLoggedEvent {
    private final Long ledgerId;
    private final String accountType; // "REVENUE", "USER_BALANCE"
    private final BigDecimal amount;
    private final ZonedDateTime occurredAt;

    public LedgerEntryLoggedEvent(Long ledgerId, String accountType, BigDecimal amount) {
        this.ledgerId = ledgerId;
        this.accountType = accountType;
        this.amount = amount;
        this.occurredAt = ZonedDateTime.now();
    }

    public Long getLedgerId() { return ledgerId; }
    public String getAccountType() { return accountType; }
    public BigDecimal getAmount() { return amount; }
    public ZonedDateTime getOccurredAt() { return occurredAt; }
}

