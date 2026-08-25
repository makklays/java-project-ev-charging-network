package com.techmatrix18.token.infrastructure.http;

import com.techmatrix18.token.application.command.RevokeTokenCommand;
import jakarta.validation.constraints.NotBlank;

/**
 * RevokeTokenRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record RevokeTokenRequest(
        @NotBlank(message = "Access token cannot be empty") String token
) {
    RevokeTokenCommand toCommand() {
        return new RevokeTokenCommand(
                this.token
        );
    }
}

