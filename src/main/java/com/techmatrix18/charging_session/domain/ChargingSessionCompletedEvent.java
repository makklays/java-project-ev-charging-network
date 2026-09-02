package com.techmatrix18.charging_session.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * ChargingSessionCompletedEvent
 * Доменное событие штатного или аварийного окончания зарядной сессии.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class ChargingSessionCompletedEvent {
    private final Long sessionId;
    private final BigDecimal totalConsumedKwh;
    private final ZonedDateTime timestamp;

    public ChargingSessionCompletedEvent(Long sessionId, BigDecimal totalConsumedKwh) {
        this.sessionId = sessionId;
        this.totalConsumedKwh = totalConsumedKwh;
        this.timestamp = ZonedDateTime.now();
    }

    public Long getSessionId() { return sessionId; }
    public BigDecimal getTotalConsumedKwh() { return totalConsumedKwh; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}

