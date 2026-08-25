package com.techmatrix18.user.application.service;

import com.techmatrix18.user.application.command.UpdateAvatarCommand;
import com.techmatrix18.user.application.port.in.UpdateAvatarUseCase;
import com.techmatrix18.user.application.port.out.UserRepository;
import com.techmatrix18.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpdateAvatarService
 * Реализация бизнес-логики обновления аватара пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

@Service
public class UpdateAvatarService implements UpdateAvatarUseCase {

    private final UserRepository userRepository;

    public UpdateAvatarService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void updateAvatar(UpdateAvatarCommand command) {
        // 1. Извлекаем доменную модель пользователя из базы данных
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + command.userId()));

        // 2. Делегируем изменение ссылки на аватар доменной логике внутри сущности User
        user.updateAvatar(command.avatarUrl());

        // 3. Сохраняем измененный объект обратно в базу данных
        userRepository.save(user);
    }
}

