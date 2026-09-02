package com.techmatrix18.evse_point.domain;

import java.time.ZonedDateTime;

/**
 * EvseStatusChangedEvent
 * Доменное событие смены состояния физического порта (AVAILABLE, PREPARING, CHARGING, FAULTED).
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class EvseStatusChangedEvent {
    private final Long evseId;
    private final String oldStatus;
    private final String newStatus;
    private final ZonedDateTime timestamp;

    public EvseStatusChangedEvent(Long evseId, String oldStatus, String newStatus) {
        this.evseId = evseId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.timestamp = ZonedDateTime.now();
    }

    public Long getEvseId() { return evseId; }
    public String getOldStatus() { return oldStatus; }
    public String getNewStatus() { return newStatus; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}

