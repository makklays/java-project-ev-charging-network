package com.techmatrix18.token.application.command;

import java.util.Objects;

/**
 * IssueTokenCommand
 * Безопасная генерация и сохранение новой пары JWT-токенов в базе данных
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

public record IssueTokenCommand(
        Long userId,
        String token,
        String refreshToken,
        String ipAddress,
        String userAgent,
        int tokenTtlMinutes,
        int refreshTtlDays
) {
    public IssueTokenCommand {
        Objects.requireNonNull(userId, "User ID is required to issue tokens");

        Objects.requireNonNull(token, "Access token string cannot be null");
        if (token.isBlank()) throw new IllegalArgumentException("Access token cannot be empty");

        Objects.requireNonNull(refreshToken, "Refresh token string cannot be null");
        if (refreshToken.isBlank()) throw new IllegalArgumentException("Refresh token cannot be empty");

        if (tokenTtlMinutes <= 0) throw new IllegalArgumentException("Token TTL must be positive");
        if (refreshTtlDays <= 0) throw new IllegalArgumentException("Refresh TTL must be positive");
    }
}

