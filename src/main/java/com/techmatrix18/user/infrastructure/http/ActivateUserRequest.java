package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.ActivateUserCommand;
import jakarta.validation.constraints.NotNull;

/**
 * ActivateUserRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record ActivateUserRequest(
        @NotNull(message = "User ID is required for activation") Long userId
) {
    public ActivateUserCommand toCommand() {
        return new ActivateUserCommand(this.userId);
    }
}

