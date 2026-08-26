-- v3__create_user_wallets_table.sql
-- Migration #3: create table user_wallets

-- Creating table 'user_wallets' for storing main token data
-- Таблица кошельков пользователей сети EV Charging network с поддержкой оптимистической блокировки
CREATE TABLE IF NOT EXISTS user_wallets (
    id          UUID PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id),
    balance     NUMERIC(12,4) NOT NULL DEFAULT 0.0000,
    version     BIGINT NOT NULL DEFAULT 0, -- Версия записи для паттерна Optimistic Locking (предотвращение Race Conditions)
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индекс для оптимизации проверок баланса и частых апдейтов
CREATE INDEX IF NOT EXISTS idx_user_wallets_user_id ON user_wallets(user_id);

