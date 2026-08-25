package com.techmatrix18.user.application.command;

import java.util.Objects;

/**
 * ActivateUserCommand
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record ActivateUserCommand(
        Long userId
) {
    public ActivateUserCommand {
        Objects.requireNonNull(userId, "User ID is required for activation");
    }
}

