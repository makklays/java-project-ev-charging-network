package com.techmatrix18.user.application.command;

import com.techmatrix18.user.domain.Gender;
import java.time.LocalDate;
import java.util.Objects;

/**
 * RegisterUserCommand
 * Используем Java Record для неизменяемых DTO команд
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

public record RegisterUserCommand(
        String username,
        String email,
        String password,
        String nickname,
        String mobile,
        Gender gender,
        LocalDate birthDate
) {
    public RegisterUserCommand {
        Objects.requireNonNull(email, "Email is required for registration");
        Objects.requireNonNull(password, "Password is required for registration");
        Objects.requireNonNull(nickname, "Nickname is required for registration");
    }
}

