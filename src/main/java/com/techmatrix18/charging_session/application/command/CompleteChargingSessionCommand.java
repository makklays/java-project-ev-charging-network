package com.techmatrix18.charging_session.application.command;

import java.util.Objects;

/**
 * CompleteChargingSessionCommand
 * Штатный успешный финиш сессии
 * Команда для штатного успешного закрытия и финализации зарядной сессии
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record CompleteChargingSessionCommand(
        Long sessionId
) {
    public CompleteChargingSessionCommand {
        Objects.requireNonNull(sessionId, "Session ID is required to complete charging process");
    }
}

