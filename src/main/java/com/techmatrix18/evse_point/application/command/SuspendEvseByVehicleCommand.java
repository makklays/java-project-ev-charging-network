package com.techmatrix18.evse_point.application.command;

import java.util.Objects;

/**
 * SuspendEvseByVehicleCommand
 * Телеметрическая команда (IoT/Kafka), фиксирующая приостановку зарядки со стороны электромобиля
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record SuspendEvseByVehicleCommand(
        Long evseId,
        Long sessionId
) {
    public SuspendEvseByVehicleCommand {
        Objects.requireNonNull(evseId, "EVSE ID is required to suspend charging process");
        Objects.requireNonNull(sessionId, "Session ID is required to notify billing system");
    }
}

