package com.techmatrix18.charging_session.application.port.in;

import com.techmatrix18.charging_session.application.command.FailChargingSessionCommand;

/**
 * FailChargingSessionUseCase
 * Входной порт для аварийного закрытия транзакции
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface FailChargingSessionUseCase {
    void failSession(FailChargingSessionCommand command);
}

