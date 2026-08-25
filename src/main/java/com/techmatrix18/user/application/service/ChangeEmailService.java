package com.techmatrix18.user.application.service;

import com.techmatrix18.user.application.command.ChangeEmailCommand;
import com.techmatrix18.user.application.port.in.ChangeEmailUseCase;
import com.techmatrix18.user.application.port.out.UserRepository;
import com.techmatrix18.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ChangeEmailService
 * Реализация бизнес-логики изменения email пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

@Service
public class ChangeEmailService implements ChangeEmailUseCase {

    private final UserRepository userRepository;

    public ChangeEmailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void changeEmail(ChangeEmailCommand command) {
        // 1. Проверяем, не занят ли новый email в системе
        if (userRepository.existsByEmail(command.newEmail())) {
            throw new IllegalArgumentException("Email '" + command.newEmail() + "' is already taken");
        }

        // 2. Находим пользователя, который хочет изменить email
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + command.userId()));

        // 3. Вызываем доменный метод смены email (его мы добавим в сущность User)
        user.changeEmail(command.newEmail());

        // 4. Сохраняем обновленные данные в базу
        userRepository.save(user);
    }
}

