package com.techmatrix18.charging_session.application.port.in;

import com.techmatrix18.charging_session.application.command.StartChargingSessionCommand;

/**
 * StartChargingSessionUseCase
 * Входной порт для запуска новой зарядной сессии
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface StartChargingSessionUseCase {
    void startSession(StartChargingSessionCommand command);
}

