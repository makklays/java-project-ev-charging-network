package com.techmatrix18.user.application.command;

import java.util.Objects;

/**
 * ChangeEmailCommand
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record ChangeEmailCommand(
        Long userId,
        String newEmail
) {
    public ChangeEmailCommand {
        Objects.requireNonNull(userId, "User ID is required for changing email");
        Objects.requireNonNull(newEmail, "New email string cannot be null");
        if (newEmail.isBlank()) {
            throw new IllegalArgumentException("New email cannot be empty");
        }
    }
}

