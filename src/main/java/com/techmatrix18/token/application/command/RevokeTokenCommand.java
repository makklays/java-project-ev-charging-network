package com.techmatrix18.token.application.command;

import java.util.Objects;

/**
 * RevokeTokenCommand
 * Аннулирование (отзыв) сессии в базе данных при разлогине (Logout)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

public record RevokeTokenCommand(
        String token
) {
    public RevokeTokenCommand {
        Objects.requireNonNull(token, "Token is required to revoke session");
        if (token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be empty");
        }
    }
}

