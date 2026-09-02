package com.techmatrix18.ledger_audit_log.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * LedgerCorrectionCreatedEvent
 * Доменное событие ручного административного исправления баланса книги.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class LedgerCorrectionCreatedEvent {
    private final Long correctionId;
    private final Long originalLedgerId;
    private final BigDecimal correctionAmount;
    private final ZonedDateTime occurredAt;

    public LedgerCorrectionCreatedEvent(Long correctionId, Long originalLedgerId, BigDecimal correctionAmount) {
        this.correctionId = correctionId;
        this.originalLedgerId = originalLedgerId;
        this.correctionAmount = correctionAmount;
        this.occurredAt = ZonedDateTime.now();
    }

    public Long getCorrectionId() { return correctionId; }
    public Long getOriginalLedgerId() { return originalLedgerId; }
    public BigDecimal getCorrectionAmount() { return correctionAmount; }
    public ZonedDateTime getOccurredAt() { return occurredAt; }
}

