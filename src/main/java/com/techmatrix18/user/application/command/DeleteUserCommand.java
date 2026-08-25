package com.techmatrix18.user.application.command;

import java.util.Objects;

/**
 * DeleteUserCommand
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record DeleteUserCommand(
        Long userId
) {
    public DeleteUserCommand {
        Objects.requireNonNull(userId, "User ID is required for deletion");
    }
}

