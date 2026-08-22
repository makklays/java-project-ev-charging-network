package com.techmatrix18.user.application.service;

import com.techmatrix18.user.application.command.RegisterUserCommand;
import com.techmatrix18.user.application.port.in.RegisterUserUseCase;
import com.techmatrix18.user.application.port.out.UserRepository;
import com.techmatrix18.user.domain.BaseRole;
import com.techmatrix18.user.domain.User;
import com.techmatrix18.user.domain.UserStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RegisterUserService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;

    public RegisterUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional // Транзакция открывается на уровне Use Case
    public User register(RegisterUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("User with this email already exists: " + command.email());
        }
        if (command.username() != null && userRepository.existsByUsername(command.username())) {
            throw new IllegalArgumentException("Username already taken: " + command.username());
        }

        // Создаем чистый доменный объект через конструктор
        User newUser = new User(
            command.username(),
            command.email(),
            BaseRole.USER,
            command.mobile(),
            command.nickname(),
            command.gender(),
            null, // avatarUrl изначально пустой
            command.birthDate(),
            null, // bio изначально пустое
            UserStatus.DRIVER,
            command.password() // Здесь в будущем добавится кодирование пароля через порт хэширования
        );

        // Сохраняем в БД через исходящий порт
        return userRepository.save(newUser);
    }
}

