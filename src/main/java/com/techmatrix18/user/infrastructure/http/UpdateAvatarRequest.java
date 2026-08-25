package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.UpdateAvatarCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

/**
 * UpdateAvatarRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record UpdateAvatarRequest(
        @NotNull(message = "User ID is required to update avatar") Long userId,
        @NotBlank(message = "Avatar URL cannot be empty")
        @URL(message = "Avatar URL must be a valid web link")
        String avatarUrl
) {
    public UpdateAvatarCommand toCommand() {
        return new UpdateAvatarCommand(this.userId, this.avatarUrl);
    }
}

