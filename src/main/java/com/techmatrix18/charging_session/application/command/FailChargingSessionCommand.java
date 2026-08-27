package com.techmatrix18.charging_session.application.command;

import java.util.Objects;

/**
 * FailChargingSessionCommand
 * Аварийное прерывание сессии при сбоях IoT
 * Команда для аварийного закрытия зарядной сессии при возникновении критических сбоев оборудования
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record FailChargingSessionCommand(
        Long sessionId
) {
    public FailChargingSessionCommand {
        Objects.requireNonNull(sessionId, "Session ID is required to process a session failure");
    }
}

