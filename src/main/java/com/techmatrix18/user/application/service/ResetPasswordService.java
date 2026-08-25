package com.techmatrix18.user.application.service;

import com.techmatrix18.user.application.command.ResetPasswordCommand;
import com.techmatrix18.user.application.port.in.ResetPasswordUseCase;
import com.techmatrix18.user.application.port.out.PasswordHash;
import com.techmatrix18.user.application.port.out.UserRepository;
import com.techmatrix18.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ResetPasswordService
 * Реализация бизнес-логики сброса пароля по временному токену
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

@Service
public class ResetPasswordService implements ResetPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHash passwordHash;
    // Опционально: здесь также внедряется порт для работы с токенами сброса,
    // например: private final PasswordResetTokenPort tokenPort;

    public ResetPasswordService(UserRepository userRepository, PasswordHash passwordHash) {
        this.userRepository = userRepository;
        this.passwordHash = passwordHash;
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordCommand command) {
        // 1. Бизнес-логика: Находим токен сброса в базе данных и проверяем его валидность.
        // (Для примера реализуем упрощенный сценарий, когда токен привязан к поиску пользователя,
        // но в реальном проекте вы достанете User ID из сущности токена)

        // Допустим, мы ищем пользователя, у которого сейчас активен данный токен сброса
        User user = userRepository.findByResetToken(command.token())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired password reset token"));

        // 2. Хешируем новый пароль через наш выходной порт шифрования
        String hashedPassword = passwordHash.encode(command.newPassword());

        // 3. Вызываем доменный метод смены пароля внутри сущности User
        user.changePassword(hashedPassword);

        // 4. Опционально: аннулируем токен, чтобы его нельзя было использовать дважды
        // tokenPort.invalidateToken(command.token());

        // 5. Сохраняем обновленного пользователя
        userRepository.save(user);
    }
}

