package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.ResetPasswordCommand;
import com.techmatrix18.user.infrastructure.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ResetPasswordRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record ResetPasswordRequest(
        @NotBlank(message = "Reset token is required") String token,
        @NotBlank(message = "New password cannot be empty")
        @Size(min = 8, message = "New password must be at least 8 characters long")
        @ValidPassword  // Использование кастомной валидации пароля
        String newPassword
) {
    public ResetPasswordCommand toCommand() {
        return new ResetPasswordCommand(this.token, this.newPassword);
    }
}

