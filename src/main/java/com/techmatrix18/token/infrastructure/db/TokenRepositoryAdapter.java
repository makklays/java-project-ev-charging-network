package com.techmatrix18.token.infrastructure.db;

import com.techmatrix18.token.application.port.out.TokenRepository;
import com.techmatrix18.token.domain.Token;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * TokenRepositoryAdapter
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

@Component
public class TokenRepositoryAdapter implements TokenRepository {

    private final JpaTokenRepository jpaTokenRepository;

    public TokenRepositoryAdapter(JpaTokenRepository jpaTokenRepository) {
        this.jpaTokenRepository = jpaTokenRepository;
    }

    @Override
    public Token save(Token token) {
        // Конвертируем чистый домен в JPA Entity для Hibernate
        TokenEntity entity = TokenEntity.fromDomain(token);
        TokenEntity savedEntity = jpaTokenRepository.save(entity);
        // Возвращаем в слой application чистый доменный объект
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return jpaTokenRepository.findByToken(token)
                .map(TokenEntity::toDomain);
    }

    @Override
    public Optional<Token> findByRefreshToken(String refreshToken) {
        return jpaTokenRepository.findByRefreshToken(refreshToken)
                .map(TokenEntity::toDomain);
    }
}

