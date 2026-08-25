package com.techmatrix18.user.application.service;

import com.techmatrix18.user.application.command.ActivateUserCommand;
import com.techmatrix18.user.application.port.in.ActivateUserUseCase;
import com.techmatrix18.user.application.port.out.UserRepository;
import com.techmatrix18.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ActivateUserService
 * Реализация бизнес-логики активации пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

@Service
public class ActivateUserService implements ActivateUserUseCase {

    private final UserRepository userRepository;

    public ActivateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional // Гарантирует атомарность обновления в базе данных
    public void activateUser(ActivateUserCommand command) {
        // Поиск пользователя через выходной порт
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + command.userId()));

        // Вызов бизнес-метода внутри самой богатой доменной модели User
        user.activate();

        // Сохранение обновленного состояния обратно в базу данных
        userRepository.save(user);
    }
}

