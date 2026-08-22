package com.techmatrix18.user.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * JpaUserRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}

