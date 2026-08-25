package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.UpdateUserCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * UpdateUserRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record UpdateUserRequest(
        @NotNull(message = "User ID is required to update profile") Long userId,
        @NotBlank(message = "Nickname is required to update profile") String nickname,
        String bio,
        String avatarUrl
) {
    public UpdateUserCommand toCommand() {
        return new UpdateUserCommand(
            this.userId,
            this.nickname,
            this.bio,
            this.avatarUrl
        );
    }
}

