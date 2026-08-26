package com.techmatrix18.user_wallet.application.port.out;

import com.techmatrix18.user_wallet.domain.UserWallet;
import java.util.Optional;
import java.util.UUID;

/**
 * UserWalletRepositoryPort
 * Выходной порт для управления постоянным хранением сущности UserWallet
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 26.08.2026
 */

public interface UserWalletRepository {
    UserWallet save(UserWallet wallet);
    Optional<UserWallet> findById(UUID id);
    Optional<UserWallet> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}

