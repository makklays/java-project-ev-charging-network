package com.techmatrix18.user.application.command;

import java.util.Objects;

/**
 * ResetPasswordCommand
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record ResetPasswordCommand(
        String token,
        String newPassword
) {
    public ResetPasswordCommand {
        Objects.requireNonNull(token, "Reset token cannot be null");
        if (token.isBlank()) throw new IllegalArgumentException("Reset token cannot be empty");

        Objects.requireNonNull(newPassword, "New password cannot be null");
        if (newPassword.isBlank()) throw new IllegalArgumentException("New password cannot be empty");
    }
}

