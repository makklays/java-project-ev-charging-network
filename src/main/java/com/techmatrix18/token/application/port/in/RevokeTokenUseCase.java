package com.techmatrix18.token.application.port.in;

import com.techmatrix18.token.application.command.RevokeTokenCommand;

/**
 * Входящий порт (Use Case) исключительно для сценария отзыва токена (Logout).
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

public interface RevokeTokenUseCase {

    // Аннулирует текущую сессию безопасности, делая токен недействительным.
    void revokeToken(RevokeTokenCommand command);
}

