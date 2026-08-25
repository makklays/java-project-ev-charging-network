package com.techmatrix18.user.application.port.in;

import com.techmatrix18.user.application.command.BlockUserCommand;

/**
 * BlockUserUseCase
 * Входной порт для блокировки пользователя модератором или администратором
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public interface BlockUserUseCase {
    void blockUser(BlockUserCommand command);
}

