package com.techmatrix18.user.application.port.in;

import com.techmatrix18.user.application.command.UpdateMobileCommand;

/**
 * UpdateMobileUseCase
 * Входной порт для обновления номера мобильного телефона пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public interface UpdateMobileUseCase {
    void updateMobile(UpdateMobileCommand command);
}

