package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.ChangeEmailCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * ChangeEmailRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record ChangeEmailRequest(
        @NotNull(message = "User ID is required to change email") Long userId,
        @NotBlank(message = "New email cannot be empty")
        @Email(message = "Invalid email format") String newEmail
) {
    public ChangeEmailCommand toCommand() {
        return new ChangeEmailCommand(this.userId, this.newEmail);
    }
}

