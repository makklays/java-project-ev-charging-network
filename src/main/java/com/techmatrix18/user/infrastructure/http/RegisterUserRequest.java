package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.RegisterUserCommand;
import com.techmatrix18.user.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

/**
 * RegisterUserRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record RegisterUserRequest(
        String username,
        @NotBlank(message = "Email is required for registration") String email,
        @NotBlank(message = "Password is required for registration") String password,
        @NotBlank(message = "Nickname is required for registration") String nickname,
        String mobile,
        Gender gender,
        @Past(message = "Birth date must be in the past") LocalDate birthDate
) {
    public RegisterUserCommand toCommand() {
        return new RegisterUserCommand(
                this.username,
                this.email,
                this.password,
                this.nickname,
                this.mobile,
                this.gender,
                this.birthDate
        );
    }
}

