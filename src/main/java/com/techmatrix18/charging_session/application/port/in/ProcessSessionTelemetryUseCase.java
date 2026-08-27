package com.techmatrix18.charging_session.application.port.in;

import com.techmatrix18.charging_session.application.command.ProcessSessionTelemetryCommand;

/**
 * ProcessSessionTelemetryUseCase
 * Входной порт для обработки регулярных показаний счетчиков (OCPP MeterValues)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface ProcessSessionTelemetryUseCase {
    void processTelemetry(ProcessSessionTelemetryCommand command);
}

