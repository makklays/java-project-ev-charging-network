package com.techmatrix18.token.application.service;

import com.techmatrix18.token.application.command.RefreshTokenCommand;
import com.techmatrix18.token.application.port.in.RefreshTokenUseCase;
import com.techmatrix18.token.application.port.out.TokenRepository;
import com.techmatrix18.token.domain.Token;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RefreshTokenService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

@Service
public class RefreshTokenService implements RefreshTokenUseCase {

    private final TokenRepository tokenRepository;

    public RefreshTokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional // Транзакция открывается на уровне конкретного бизнес-сценария
    public Token refreshTokens(RefreshTokenCommand command) {
        // 1. Извлекаем старую сессию из БД через исходящий порт
        Token existingToken = tokenRepository.findByRefreshToken(command.refreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        // 2. Делегируем проверку инвариантов безопасности в чистый домен
        if (existingToken.isRevoked() || existingToken.isRefreshExpired()) {
            throw new IllegalStateException("Refresh token is already revoked or expired");
        }

        // 3. Изменяем состояние доменного агрегата (отзываем старый токен)
        existingToken.revoke();

        // 4. Сохраняем измененное состояние в базу данных
        tokenRepository.save(existingToken);

        // При полноценной интеграции здесь будет создание и возврат новой пары токенов
        return existingToken;
    }
}

