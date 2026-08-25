package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.BlockUserCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * BlockUserRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record BlockUserRequest(
        @NotNull(message = "User ID is required for blocking") Long userId,
        @NotBlank(message = "Reason for blocking cannot be empty") String reason
) {
    public BlockUserCommand toCommand() {
        return new BlockUserCommand(this.userId, this.reason);
    }
}

