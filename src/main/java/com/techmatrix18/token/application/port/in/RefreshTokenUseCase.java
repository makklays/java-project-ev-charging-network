package com.techmatrix18.token.application.port.in;

import com.techmatrix18.token.application.command.RefreshTokenCommand;
import com.techmatrix18.token.domain.Token;

/**
 * Входящий порт (Use Case) исключительно для сценария обновления (ротации) токенов.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

public interface RefreshTokenUseCase {

    // Проверяет старый Refresh-токен и возвращает обновленную сессию безопасности.
    Token refreshTokens(RefreshTokenCommand command);
}

