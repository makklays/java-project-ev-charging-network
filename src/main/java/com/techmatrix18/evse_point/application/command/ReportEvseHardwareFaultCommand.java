package com.techmatrix18.evse_point.application.command;

import java.util.Objects;

/**
 * ReportEvseHardwareFaultCommand
 * Телеметрическая команда (IoT/Kafka), фиксирующая аппаратную поломку или критический сбой точки зарядки
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record ReportEvseHardwareFaultCommand(
        Long evseId,
        String errorCode
) {
    public ReportEvseHardwareFaultCommand {
        Objects.requireNonNull(evseId, "EVSE ID is required to log a hardware fault");
        Objects.requireNonNull(errorCode, "Error code/description cannot be null");

        if (errorCode.isBlank()) {
            throw new IllegalArgumentException("Error code description cannot be empty");
        }
    }
}

