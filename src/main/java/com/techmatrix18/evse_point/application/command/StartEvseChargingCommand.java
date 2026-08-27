package com.techmatrix18.evse_point.application.command;

import java.util.Objects;

/**
 * StartEvseChargingCommand
 * Телеметрическая команда (IoT/Kafka), фиксирующая успешный старт подачи электроэнергии в автомобиль
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record StartEvseChargingCommand(
        Long evseId,
        Long sessionId
) {
    public StartEvseChargingCommand {
        Objects.requireNonNull(evseId, "EVSE ID is required to start charging");
        Objects.requireNonNull(sessionId, "Session ID is required to bind charging process to billing");
    }
}

