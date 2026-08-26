package com.techmatrix18.user_wallet.infrastructure.db;

import com.techmatrix18.user_wallet.domain.UserWallet;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * UserWalletEntity
 * JPA Сущность для таблицы "user_wallets" (Слой инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Entity
@Table(name = "user_wallets")
public class UserWalletEntity {

    @Id
    private UUID id; // Уникальный UUID кошелька из миграции

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId; // Ссылка на users(id)

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal balance;

    @Version // Активирует Optimistic Locking для предотвращения Race Conditions
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // --- Автоматическое управление временными метками (Жизненный цикл JPA) ---

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = ZonedDateTime.now();
        }
        this.updatedAt = ZonedDateTime.now();
    }

    @PreUpdate // Аннотация стоит НАД МЕТОДОМ, это решает вашу ошибку!
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    // --- Конструкторы ---

    public UserWalletEntity() {
    }

    // --- Маппинг: Из чистого Домена в JPA Entity (Для сохранения в БД) ---
    public static UserWalletEntity fromDomain(UserWallet wallet) {
        if (wallet == null) return null;

        UserWalletEntity entity = new UserWalletEntity();
        entity.id = wallet.getId();
        entity.userId = wallet.getUserId();
        entity.balance = wallet.getBalance();
        entity.version = wallet.getVersion(); // Передаем текущую версию
        entity.createdAt = wallet.getCreatedAt();
        entity.updatedAt = wallet.getUpdatedAt();
        return entity;
    }

    // --- Маппинг: Из JPA Entity в чистый Домен (Для чтения из БД) ---
    public UserWallet toDomain() {
        return new UserWallet(
                this.id,
                this.userId,
                this.balance,
                this.version, // Возвращаем версию в домен для контроля блокировок
                this.createdAt,
                this.updatedAt
        );
    }

    // --- Стандартные геттеры и сеттеры для JPA ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}

