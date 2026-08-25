package com.techmatrix18.user.application.port.in;

import com.techmatrix18.user.application.command.ChangePasswordCommand;

/**
 * ChangePasswordUseCase
 * Входной порт для изменения пароля пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public interface ChangePasswordUseCase {
    void changePassword(ChangePasswordCommand command);
}

