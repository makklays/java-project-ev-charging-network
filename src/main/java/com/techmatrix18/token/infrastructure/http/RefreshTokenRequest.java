package com.techmatrix18.token.infrastructure.http;

import com.techmatrix18.token.application.command.RefreshTokenCommand;
import jakarta.validation.constraints.NotBlank;

/**
 * RefreshTokenRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token cannot be empty") String refreshToken,
        String ipAddress,
        String userAgent
) {
    public RefreshTokenCommand toCommand() {
        return new RefreshTokenCommand(
                this.refreshToken,
                this.ipAddress,
                this.userAgent
        );
    }
}

