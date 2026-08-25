package com.techmatrix18.user.application.port.in;

import com.techmatrix18.user.application.command.UpdateUserCommand;
import com.techmatrix18.user.domain.User;

/**
 * updateProfile
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

public interface UpdateUserUseCase {
    User updateUser(UpdateUserCommand command);
}

