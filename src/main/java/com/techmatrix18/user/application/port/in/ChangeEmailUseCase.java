package com.techmatrix18.user.application.port.in;

import com.techmatrix18.user.application.command.ChangeEmailCommand;

/**
 * ChangeEmailUseCase
 * Входной порт для изменения email пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public interface ChangeEmailUseCase {
    void changeEmail(ChangeEmailCommand command);
}

