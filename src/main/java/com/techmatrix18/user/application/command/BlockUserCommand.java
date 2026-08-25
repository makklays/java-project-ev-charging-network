package com.techmatrix18.user.application.command;

import java.util.Objects;

/**
 * BlockUserCommand
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record BlockUserCommand(
        Long userId,
        String reason
) {
    public BlockUserCommand {
        Objects.requireNonNull(userId, "User ID is required for blocking");
        Objects.requireNonNull(reason, "Reason string cannot be null");
        if (reason.isBlank()) {
            throw new IllegalArgumentException("Reason for blocking cannot be empty");
        }
    }
}

