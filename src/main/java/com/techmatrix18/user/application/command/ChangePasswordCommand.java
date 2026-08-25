package com.techmatrix18.user.application.command;

import java.util.Objects;

/**
 * ChangePasswordCommand
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record ChangePasswordCommand(
        Long userId,
        String oldPassword,
        String newPassword
) {
    public ChangePasswordCommand {
        Objects.requireNonNull(userId, "User ID is required for changing password");

        Objects.requireNonNull(oldPassword, "Old password cannot be null");
        if (oldPassword.isBlank()) throw new IllegalArgumentException("Old password cannot be empty");

        Objects.requireNonNull(newPassword, "New password cannot be null");
        if (newPassword.isBlank()) throw new IllegalArgumentException("New password cannot be empty");
    }
}

