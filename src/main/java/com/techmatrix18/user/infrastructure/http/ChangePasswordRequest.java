package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.ChangePasswordCommand;
import com.techmatrix18.user.infrastructure.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * ChangePasswordRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record ChangePasswordRequest(
        @NotNull(message = "User ID is required to change password") Long userId,
        @NotBlank(message = "Old password cannot be empty") String oldPassword,
        @NotBlank(message = "New password cannot be empty")
        @Size(min = 8, message = "New password must be at least 8 characters long")
        @ValidPassword
        String newPassword
) {
    public ChangePasswordCommand toCommand() {
        return new ChangePasswordCommand(this.userId, this.oldPassword, this.newPassword);
    }
}

