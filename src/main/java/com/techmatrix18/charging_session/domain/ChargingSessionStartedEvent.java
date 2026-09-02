package com.techmatrix18.charging_session.domain;

import java.time.ZonedDateTime;

/**
 * ChargingSessionStartedEvent
 * Доменное событие инициализации и успешного старта сессии зарядки электромобиля.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class ChargingSessionStartedEvent {
    private final Long sessionId;
    private final Long userId;
    private final Long connectorId;
    private final ZonedDateTime timestamp;

    public ChargingSessionStartedEvent(Long sessionId, Long userId, Long connectorId) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.connectorId = connectorId;
        this.timestamp = ZonedDateTime.now();
    }

    public Long getSessionId() { return sessionId; }
    public Long getUserId() { return userId; }
    public Long getConnectorId() { return connectorId; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}

