package com.techmatrix18.user_wallet.infrastructure.db;

import com.techmatrix18.user_wallet.application.port.out.UserWalletRepository;
import com.techmatrix18.user_wallet.domain.UserWallet;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

/**
 * UserWalletRepositoryAdapter
 * Выходной адаптер для управления постоянным хранением кошельков в JPA
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Component
public class UserWalletRepositoryAdapter implements UserWalletRepository {

    private final JpaUserWalletRepository repository;

    // Внедряем Spring Data репозиторий через конструктор
    public UserWalletRepositoryAdapter(JpaUserWalletRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserWallet save(UserWallet wallet) {
        // 1. Переводим чистый Домен в JPA Entity
        UserWalletEntity entity = UserWalletEntity.fromDomain(wallet);

        // 2. Сохраняем в базу данных через Spring Data
        UserWalletEntity savedEntity = repository.save(entity);

        // 3. Возвращаем обратно доменный объект
        return savedEntity.toDomain();
    }

    @Override
    public Optional<UserWallet> findById(UUID id) {
        // Достаем Entity и, если она есть, маппим в Домен
        return repository.findById(id)
                .map(UserWalletEntity::toDomain);
    }

    @Override
    public Optional<UserWallet> findByUserId(Long userId) {
        // Достаем Entity по user_id и маппим в Домен
        return repository.findByUserId(userId)
                .map(UserWalletEntity::toDomain);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        // Просто перенаправляем проверку в базу
        return repository.existsByUserId(userId);
    }
}

