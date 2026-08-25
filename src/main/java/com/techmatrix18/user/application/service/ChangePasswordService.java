package com.techmatrix18.user.application.service;

import com.techmatrix18.user.application.command.ChangePasswordCommand;
import com.techmatrix18.user.application.port.in.ChangePasswordUseCase;
import com.techmatrix18.user.application.port.out.UserRepository;
import com.techmatrix18.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ChangePasswordService
 * Реализация бизнес-логики изменения пароля пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

@Service
public class ChangePasswordService implements ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordCommand command) {
        // 1. Находим пользователя в базе данных
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + command.userId()));

        // 2. Нестандартная доменная проверка: сверяем старый пароль с текущим хешем
        // (Предполагается, что у вашей сущности User есть метод user.getPassword())
        if (!passwordEncoder.matches(command.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }

        // 3. Хешируем новый пароль
        String hashedNewPassword = passwordEncoder.encode(command.newPassword());

        // 4. Передаем безопасный хеш в метод доменной сущности User
        user.changePassword(hashedNewPassword);

        // 5. Сохраняем обновленные данные
        userRepository.save(user);
    }
}

