package com.techmatrix18.user_wallet.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * JpaUserWalletRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Repository
public interface JpaUserWalletRepository extends JpaRepository<UserWalletEntity, UUID> {
    // Метод для поиска кошелька по ID пользователя
    Optional<UserWalletEntity> findByUserId(Long userId);

    // Метод для проверки существования кошелька
    boolean existsByUserId(Long userId);
}

