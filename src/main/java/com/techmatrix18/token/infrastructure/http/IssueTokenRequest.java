package com.techmatrix18.token.infrastructure.http;

import com.techmatrix18.token.application.command.IssueTokenCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * IssueTokenRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record IssueTokenRequest(
        @NotNull(message = "User ID is required") Long userId,
        @NotBlank(message = "Access token cannot be empty") String token,
        @NotBlank(message = "Refresh token cannot be empty") String refreshToken,
        String ipAddress,
        String userAgent,
        @Positive(message = "Token TTL must be positive") int tokenTtlMinutes,
        @Positive(message = "Refresh TTL must be positive") int refreshTtlDays
) {
    // Метод маппинга в вашу чистую доменную команду
    public IssueTokenCommand toCommand() {
        return new IssueTokenCommand(
                this.userId,
                this.token,
                this.refreshToken,
                this.ipAddress,
                this.userAgent,
                this.tokenTtlMinutes,
                this.refreshTtlDays
        );
    }
}

