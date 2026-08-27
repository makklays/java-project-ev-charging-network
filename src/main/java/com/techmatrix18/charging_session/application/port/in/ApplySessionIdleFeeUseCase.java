package com.techmatrix18.charging_session.application.port.in;

import com.techmatrix18.charging_session.application.command.ApplySessionIdleFeeCommand;

/**
 * ApplySessionIdleFeeUseCase
 * Входной порт для начисления пени за оккупацию парковочного кабеля
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface ApplySessionIdleFeeUseCase {
    void applyIdleFee(ApplySessionIdleFeeCommand command);
}

