package com.techmatrix18.user.application.port.in;

import com.techmatrix18.user.application.command.ChangeUserRoleCommand;

/**
 * ChangeUserRoleUseCase
 * Входной порт для изменения роли пользователя администратором
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public interface ChangeUserRoleUseCase {
    void changeUserRole(ChangeUserRoleCommand command);
}

