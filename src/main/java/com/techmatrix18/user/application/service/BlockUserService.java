package com.techmatrix18.user.application.service;

import com.techmatrix18.user.application.command.BlockUserCommand;
import com.techmatrix18.user.application.port.in.BlockUserUseCase;
import com.techmatrix18.user.application.port.out.UserRepository;
import com.techmatrix18.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * BlockUserService
 * Реализация бизнес-логики блокировки пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

@Service
public class BlockUserService implements BlockUserUseCase {

    private final UserRepository userRepository;

    public BlockUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void blockUser(BlockUserCommand command) {
        // 1. Извлекаем доменную модель пользователя
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + command.userId()));

        // 2. Передаем управление и причину блокировки в доменную логику
        user.block(command.reason());

        // 3. Сохраняем обновленную сущность обратно в базу
        userRepository.save(user);
    }
}

