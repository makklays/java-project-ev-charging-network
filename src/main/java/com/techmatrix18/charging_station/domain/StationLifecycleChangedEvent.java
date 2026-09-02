package com.techmatrix18.charging_station.domain;

import java.time.ZonedDateTime;

/**
 * StationLifecycleChangedEvent
 * Доменное событие изменения системного статуса всей локации (ONLINE, OFFLINE, MAINTENANCE).
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class StationLifecycleChangedEvent {
    private final Long stationId;
    private final String targetStatus;
    private final ZonedDateTime timestamp;

    public StationLifecycleChangedEvent(Long stationId, String targetStatus) {
        this.stationId = stationId;
        this.targetStatus = targetStatus;
        this.timestamp = ZonedDateTime.now();
    }

    public Long getStationId() { return stationId; }
    public String getTargetStatus() { return targetStatus; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}

