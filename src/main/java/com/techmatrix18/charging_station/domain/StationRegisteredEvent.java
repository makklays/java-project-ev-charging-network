package com.techmatrix18.charging_station.domain;

import java.time.ZonedDateTime;

/**
 * StationRegisteredEvent
 * Доменное событие создания и регистрации новой зарядной локации в сети.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class StationRegisteredEvent {
    private final Long stationId;
    private final String name;
    private final ZonedDateTime timestamp;

    public StationRegisteredEvent(Long stationId, String name) {
        this.stationId = stationId;
        this.name = name;
        this.timestamp = ZonedDateTime.now();
    }

    public Long getStationId() { return stationId; }
    public String getName() { return name; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}

