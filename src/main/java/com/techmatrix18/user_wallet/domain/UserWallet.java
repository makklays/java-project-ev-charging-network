package com.techmatrix18.user_wallet.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * UserWallet
 * Чистая доменная сущность кошелька пользователя сети EV Charging
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.3
 * @since 26.08.2026
 */

public class UserWallet {
    private final UUID id;
    private final Long userId;
    private BigDecimal balance;
    private final Long version;
    private final ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    // Список событий, произошедших с агрегатом в памяти
    private final List<Object> domainEvents = new ArrayList<>();

    // Конструктор для первичного бизнес-создания кошелька (например, при регистрации пользователя)
    public UserWallet(Long userId) {
        this.id = UUID.randomUUID();
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.balance = new BigDecimal("0.0000");
        this.version = 0L;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    // Конструктор для восстановления объекта из базы данных (используется маппером в Persistence Adapter)
    public UserWallet(UUID id, Long userId, BigDecimal balance, Long version,
                      ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "Wallet ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.balance = Objects.requireNonNull(balance, "Balance cannot be null");
        this.version = Objects.requireNonNull(version, "Version cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt date cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "UpdatedAt date cannot be null");
    }

    // Бизнес-метод: Пополнение баланса (Депозит)
    public void deposit(BigDecimal amount) {
        Objects.requireNonNull(amount, "Deposit amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
        this.updatedAt = ZonedDateTime.now();

        // [OUTBOX EVENT]: Фиксируем доменное событие пополнения кошелька
        this.domainEvents.add(new WalletDepositedEvent(this.id, this.userId, amount));
    }

    // Бизнес-метод: Списание с баланса (Списание за кВт*ч или простой)
    public void withdraw(BigDecimal amount) {
        Objects.requireNonNull(amount, "Withdraw amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds for withdrawal");
        }
        this.balance = this.balance.subtract(amount);
        this.updatedAt = ZonedDateTime.now();

        // [OUTBOX EVENT]: Фиксируем доменное событие вывода/списания средств
        this.domainEvents.add(new MoneyWithdrawnEvent(this.id, this.userId, amount));
    }

    /**
     * Возвращает список всех произошедших доменных событий.
     * Обертка в unmodifiableList защищает коллекцию от случайного изменения извне домена.
     */
    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Очищает список событий после того, как инфраструктурный адаптер
     * успешно перенес их в таблицу outbox_events.
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    // --- Геттеры для маппинга в инфраструктуру ---

    public UUID getId() { return id; }
    public Long getUserId() { return userId; }
    public BigDecimal getBalance() { return balance; }
    public Long getVersion() { return version; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
}

