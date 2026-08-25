package com.techmatrix18.user.application.service;

import com.techmatrix18.user.application.command.ChangeUserRoleCommand;
import com.techmatrix18.user.application.port.in.ChangeUserRoleUseCase;
import com.techmatrix18.user.application.port.out.UserRepository;
import com.techmatrix18.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ChangeUserRoleService
 * Реализация бизнес-логики изменения роли пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

@Service
public class ChangeUserRoleService implements ChangeUserRoleUseCase {

    private final UserRepository userRepository;

    public ChangeUserRoleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void changeUserRole(ChangeUserRoleCommand command) {
        // 1. Извлекаем доменную модель пользователя из базы данных
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + command.userId()));

        // 2. Делегируем изменение роли доменной логике внутри сущности User
        user.changeRole(command.newRole());

        // 3. Сохраняем обновленное состояние в репозиторий
        userRepository.save(user);
    }
}

