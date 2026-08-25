package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.UpdateMobileCommand;
import com.techmatrix18.user.infrastructure.validation.ValidMobile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * UpdateMobileRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record UpdateMobileRequest(
        @NotNull(message = "User ID is required to update mobile number") Long userId,
        @NotBlank(message = "New mobile number cannot be empty")
        @ValidMobile // Использование кастомной валидации телефона
        String newMobile
) {
    public UpdateMobileCommand toCommand() {
        return new UpdateMobileCommand(this.userId, this.newMobile);
    }
}

