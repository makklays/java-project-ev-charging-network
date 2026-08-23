package com.techmatrix18.token.application.command;

import java.util.Objects;

/**
 * RefreshTokenCommand
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

public record RefreshTokenCommand(
        String refreshToken,
        String ipAddress,
        String userAgent
) {
    public RefreshTokenCommand {
        Objects.requireNonNull(refreshToken, "Refresh token is required for rotation");
        if (refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token cannot be empty");
        }
    }
}

