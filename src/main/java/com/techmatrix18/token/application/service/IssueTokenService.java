package com.techmatrix18.token.application.service;

import com.techmatrix18.token.application.command.IssueTokenCommand;
import com.techmatrix18.token.application.port.in.IssueTokenUseCase;
import com.techmatrix18.token.application.port.out.TokenRepository;
import com.techmatrix18.token.domain.Token;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;

/**
 * Выделенный сервис, отвечающий исключительно за выполнение сценария выпуска токенов.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

@Service
public class IssueTokenService implements IssueTokenUseCase {

    private final TokenRepository tokenRepository;

    // Внедряем только порт базы данных
    public IssueTokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional // Атомарная транзакция на уровне бизнес-сценария
    public Token issueTokens(IssueTokenCommand command) {
        // Создаем чистый доменный агрегат Token
        Token newToken = new Token(
                null, // ID сгенерирует PostgreSQL
                command.userId(),
                command.token(),
                ZonedDateTime.now().plusMinutes(command.tokenTtlMinutes()),
                command.refreshToken(),
                ZonedDateTime.now().plusDays(command.refreshTtlDays()),
                null,
                null,
                command.ipAddress(),
                command.userAgent(),
                false, // Токен активен (не отозван)
                ZonedDateTime.now(),
                ZonedDateTime.now()
        );

        // Сохраняем сессию через исходящий порт
        return tokenRepository.save(newToken);
    }
}

