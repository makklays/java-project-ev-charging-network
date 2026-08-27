package com.techmatrix18.evse_point.application.port.in;

import com.techmatrix18.evse_point.application.command.LimitEvseCurrentPowerCommand;

/**
 * LimitEvseCurrentPowerUseCase
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface LimitEvseCurrentPowerUseCase {
    void limitEvsePower(LimitEvseCurrentPowerCommand command);
}

