package com.techmatrix18.user.application.command;

import java.util.Objects;

/**
 * UpdateMobileCommand
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record UpdateMobileCommand(
        Long userId,
        String newMobile
) {
    public UpdateMobileCommand {
        Objects.requireNonNull(userId, "User ID is required to update mobile number");
        Objects.requireNonNull(newMobile, "New mobile number cannot be null");
        if (newMobile.isBlank()) {
            throw new IllegalArgumentException("New mobile number cannot be empty");
        }
    }
}

