package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.DeleteUserCommand;
import jakarta.validation.constraints.NotNull;

/**
 * DeleteUserRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record DeleteUserRequest(
        @NotNull(message = "User ID is required for deletion") Long userId
) {
    public DeleteUserCommand toCommand() {
        return new DeleteUserCommand(this.userId);
    }
}

