package com.techmatrix18.token.application.service;

import com.techmatrix18.token.application.command.RevokeTokenCommand;
import com.techmatrix18.token.application.port.in.RevokeTokenUseCase;
import com.techmatrix18.token.application.port.out.TokenRepository;
import com.techmatrix18.token.domain.Token;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RevokeTokenService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

@Service
public class RevokeTokenService implements RevokeTokenUseCase {

    private final TokenRepository tokenRepository;

    public RevokeTokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional // Атомарная транзакция для безопасного закрытия сессии
    public void revokeToken(RevokeTokenCommand command) {
        // 1. Находим токен через исходящий порт
        Token existingToken = tokenRepository.findByToken(command.token())
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));

        // 2. Делегируем бизнес-логику отзыва самому домену (меняется revoked и updatedAt)
        existingToken.revoke();

        // 3. Сохраняем измененное состояние в базу данных
        tokenRepository.save(existingToken);
    }
}

