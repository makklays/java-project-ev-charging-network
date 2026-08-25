package com.techmatrix18.token.domain;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Token
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

public class Token {
    private final Long id;
    private final Long userId;
    private final String token;
    private final ZonedDateTime expiredToken;
    private final String refreshToken;
    private final ZonedDateTime expiredRefreshToken;
    private String passwordResetToken;
    private ZonedDateTime expiredPasswordResetToken;
    private final String ipAddress;
    private final String userAgent;
    private boolean revoked;
    private final ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    // Конструктор для восстановления из БД
    public Token(Long id, Long userId, String token, ZonedDateTime expiredToken,
                 String refreshToken, ZonedDateTime expiredRefreshToken, String passwordResetToken,
                 ZonedDateTime expiredPasswordResetToken, String ipAddress, String userAgent,
                 boolean revoked, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.token = Objects.requireNonNull(token, "Token cannot be null");
        this.expiredToken = expiredToken;
        this.refreshToken = Objects.requireNonNull(refreshToken, "Refresh token cannot be null");
        this.expiredRefreshToken = expiredRefreshToken;
        this.passwordResetToken = passwordResetToken;
        this.expiredPasswordResetToken = expiredPasswordResetToken;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.revoked = revoked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- Бизнес-логика Домена ---

    public boolean isExpired() {
        return ZonedDateTime.now().isAfter(expiredToken);
    }

    public boolean isRefreshExpired() {
        return ZonedDateTime.now().isAfter(expiredRefreshToken);
    }

    // Перевод токена (например, JWT) в недействительное (отозванное) состояние.
    // При Logout или умышленной блокировке token
    public void revoke() {
        this.revoked = true;
        this.updatedAt = ZonedDateTime.now();
    }

    public void setPasswordReset(String resetToken, int expirationMinutes) {
        this.passwordResetToken = Objects.requireNonNull(resetToken);
        this.expiredPasswordResetToken = ZonedDateTime.now().plusMinutes(expirationMinutes);
        this.updatedAt = ZonedDateTime.now();
    }

    // --- Геттеры ---

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getToken() { return token; }
    public ZonedDateTime getExpiredToken() { return expiredToken; }
    public String getRefreshToken() { return refreshToken; }
    public ZonedDateTime getExpiredRefreshToken() { return expiredRefreshToken; }
    public String getPasswordResetToken() { return passwordResetToken; }
    public ZonedDateTime getExpiredPasswordResetToken() { return expiredPasswordResetToken; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public boolean isRevoked() { return revoked; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
}

