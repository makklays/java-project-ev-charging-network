package com.techmatrix18.connector.domain;

import java.time.ZonedDateTime;

/**
 * ConnectorStatusChangedEvent
 * Доменное событие изменения состояния конкретного кабеля (занят/свободен/перегрев).
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class ConnectorStatusChangedEvent {
    private final Long connectorId;
    private final String status;
    private final ZonedDateTime timestamp;

    public ConnectorStatusChangedEvent(Long connectorId, String status) {
        this.connectorId = connectorId;
        this.status = status;
        this.timestamp = ZonedDateTime.now();
    }

    public Long getConnectorId() { return connectorId; }
    public String getStatus() { return status; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}

