package com.techmatrix18.token.application.port.out;

import com.techmatrix18.token.domain.Token;
import java.util.Optional;

/**
 * TokenRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

public interface TokenRepository {

    // Сохраняет или обновляет токен сессии, возвращая чистый домен
    Token save(Token token);

    // Ищет активную сессию по стандартному JWT access-токену
    Optional<Token> findByToken(String token);

    // Ищет сессию по refresh-токену для выполнения процедуры ротации (refresh)
    Optional<Token> findByRefreshToken(String refreshToken);
}

