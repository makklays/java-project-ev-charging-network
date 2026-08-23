package com.techmatrix18.token.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * JpaTokenRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

public interface JpaTokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByToken(String token);
    Optional<TokenEntity> findByRefreshToken(String refreshToken);
}

