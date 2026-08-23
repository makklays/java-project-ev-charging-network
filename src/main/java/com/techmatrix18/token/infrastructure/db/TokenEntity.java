package com.techmatrix18.token.infrastructure.db;

import com.techmatrix18.token.domain.Token;
import jakarta.persistence.*;
import java.time.ZonedDateTime;

/**
 * TokenEntity
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

@Entity
@Table(name = "tokens")
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String token;

    @Column(name = "expired_token", nullable = false)
    private ZonedDateTime expiredToken;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "expired_refresh_token", nullable = false)
    private ZonedDateTime expiredRefreshToken;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "expired_password_reset_token")
    private ZonedDateTime expiredPasswordResetToken;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(nullable = false)
    private boolean revoked;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public TokenEntity() {}

    // Двусторонний маппинг
    public static TokenEntity fromDomain(Token domain) {
        TokenEntity entity = new TokenEntity();
        entity.id = domain.getId();
        entity.userId = domain.getUserId();
        entity.token = domain.getToken();
        entity.expiredToken = domain.getExpiredToken();
        entity.refreshToken = domain.getRefreshToken();
        entity.expiredRefreshToken = domain.getExpiredRefreshToken();
        entity.passwordResetToken = domain.getPasswordResetToken();
        entity.expiredPasswordResetToken = domain.getExpiredPasswordResetToken();
        entity.ipAddress = domain.getIpAddress();
        entity.userAgent = domain.getUserAgent();
        entity.revoked = domain.isRevoked();
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }

    public Token toDomain() {
        return new Token(id, userId, token, expiredToken, refreshToken, expiredRefreshToken,
                passwordResetToken, expiredPasswordResetToken, ipAddress, userAgent, revoked, createdAt, updatedAt);
    }
}

