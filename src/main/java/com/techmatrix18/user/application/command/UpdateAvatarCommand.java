package com.techmatrix18.user.application.command;

import java.util.Objects;

/**
 * UpdateAvatarCommand
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record UpdateAvatarCommand(
        Long userId,
        String avatarUrl
) {
    public UpdateAvatarCommand {
        Objects.requireNonNull(userId, "User ID is required to update avatar");
        Objects.requireNonNull(avatarUrl, "Avatar URL string cannot be null");
        if (avatarUrl.isBlank()) {
            throw new IllegalArgumentException("Avatar URL cannot be empty");
        }
    }
}

