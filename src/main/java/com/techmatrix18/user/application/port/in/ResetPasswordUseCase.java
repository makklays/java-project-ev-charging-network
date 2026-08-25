package com.techmatrix18.user.application.port.in;

import com.techmatrix18.user.application.command.ResetPasswordCommand;

/**
 * ResetPasswordUseCase
 * Входной порт для сброса пароля по временному токену
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public interface ResetPasswordUseCase {
    void resetPassword(ResetPasswordCommand command);
}

