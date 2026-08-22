package com.techmatrix18.user.application.command;

import java.util.Objects;

/**
 * UpdateProfileCommand
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

public record UpdateUserCommand(
        Long userId,
        String nickname,
        String bio,
        String avatarUrl
) {
    public UpdateUserCommand {
        Objects.requireNonNull(userId, "User ID is required to update profile");
        Objects.requireNonNull(nickname, "Nickname cannot be null");
        if (nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be empty");
        }
    }
}

