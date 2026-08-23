package com.techmatrix18.token.infrastructure.http;

import com.techmatrix18.token.domain.Token;

/**
 * TokenResponse
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

public record TokenResponse(
        Long id,
        Long userId,
        String token,
        String refreshToken,
        boolean revoked
) {
    public static TokenResponse fromDomain(Token token) {
        return new TokenResponse(
                token.getId(),
                token.getUserId(),
                token.getToken(),
                token.getRefreshToken(),
                token.isRevoked()
        );
    }
}

