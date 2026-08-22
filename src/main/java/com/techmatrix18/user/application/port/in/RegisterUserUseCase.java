package com.techmatrix18.user.application.port.in;

import com.techmatrix18.user.application.command.RegisterUserCommand;
import com.techmatrix18.user.domain.User;

/**
 * RegisterUserUseCase
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

public interface RegisterUserUseCase {
    User register(RegisterUserCommand command);
}

