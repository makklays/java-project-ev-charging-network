package com.techmatrix18.user.application.service;

import com.techmatrix18.user.application.command.UpdateMobileCommand;
import com.techmatrix18.user.application.port.in.UpdateMobileUseCase;
import com.techmatrix18.user.application.port.out.UserRepository;
import com.techmatrix18.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpdateMobileService
 * Реализация бизнес-логики обновления номера мобильного телефона
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

@Service
public class UpdateMobileService implements UpdateMobileUseCase {

    private final UserRepository userRepository;

    public UpdateMobileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void updateMobile(UpdateMobileCommand command) {
        // 1. Необязательная проверка уникальности (если у вас в порту есть метод existsByMobile)
        // if (userRepositoryPort.existsByMobile(command.newMobile())) {
        //     throw new IllegalArgumentException("Mobile number is already in use");
        // }

        // 2. Извлекаем доменную модель пользователя из базы данных
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + command.userId()));

        // 3. Делегируем изменение номера телефона доменной логике внутри сущности User
        user.updateMobile(command.newMobile());

        // 4. Сохраняем обновленный объект обратно в базу данных
        userRepository.save(user);
    }
}

