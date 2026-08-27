package com.techmatrix18.charging_session.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * ApplySessionIdleFeeCommand
 * Начисление штрафа/пени за простой на парковке
 * Телеметрическая/системная команда (из Крона/Kafka) на начисление штрафа за простой
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record ApplySessionIdleFeeCommand(
        Long sessionId,
        BigDecimal idlePricePerMin,
        Integer idleMinutesDelta
) {
    public ApplySessionIdleFeeCommand {
        Objects.requireNonNull(sessionId, "Session ID is required to apply idle fee");
        Objects.requireNonNull(idlePricePerMin, "Idle price per minute cannot be null");
        Objects.requireNonNull(idleMinutesDelta, "Idle minutes delta cannot be null");

        // Финтех и логическая валидация
        if (idlePricePerMin.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Idle price per minute cannot be negative");
        }
        if (idleMinutesDelta <= 0) {
            throw new IllegalArgumentException("Idle minutes delta must be a positive integer greater than zero");
        }
    }
}

