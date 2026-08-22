package com.techmatrix18.user.application.service;

import com.techmatrix18.user.application.command.UpdateUserCommand;
import com.techmatrix18.user.application.port.in.UpdateUserUseCase;
import com.techmatrix18.user.application.port.out.UserRepository;
import com.techmatrix18.user.domain.User;
import com.techmatrix18.user.domain.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpdateUserService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

@Service
public class UpdateUserService implements UpdateUserUseCase {

    private final UserRepository userRepository;

    public UpdateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional // Гарантирует атомарность операции в БД
    public User updateProfile(UpdateUserCommand command) {
        // 1. Получаем чистый доменный объект из базы данных
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));

        // 2. Делегируем бизнес-логику изменения данных внутрь доменной сущности.
        // Именно домен внутри себя инкапсулирует правила изменения и обновляет дату updatedAt.
        user.updateProfile(
                command.nickname(),
                command.bio(),
                command.avatarUrl()
        );

        // 3. Сохраняем обновленный доменный агрегат обратно через исходящий порт
        return userRepository.save(user);
    }
}

