-- V11__create_charging_transactions_table.sql
-- Migration #11: create table charging_transactions

-- Creating table 'charging_transactions' for tracking real-time EV charging sessions and billing data
-- Таблица учета зарядных транзакций и real-time биллинга сети EV Charging network
CREATE TABLE IF NOT EXISTS charging_sessions (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL,
    connector_id        BIGINT NOT NULL REFERENCES connectors(id) ON DELETE RESTRICT,

    -- Текущий статус транзакции (IN_PROGRESS, COMPLETED, SUSPENDED, FAILED)
    status              VARCHAR(50) NOT NULL DEFAULT 'IN_PROGRESS',

    -- Временные метки физического процесса
    started_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    finished_at         TIMESTAMP WITH TIME ZONE,

    -- Показания внутренних счетчиков станции в кВт*ч (используются для расчета дельты)
    start_meter_value   NUMERIC(12,3) NOT NULL,  -- Значение счетчика (кВт*ч) при старте
    last_meter_value    NUMERIC(12,3) NOT NULL,  -- Последнее полученное значение (кВт*ч) присланное станцией через IoT/Kafka

    -- Агрегированные бизнес-метрики (постоянно обновляются в процессе зарядки)
    total_kwh_consumed  NUMERIC(12,3) NOT NULL DEFAULT 0.000,
    total_energy_amount NUMERIC(12,4) NOT NULL DEFAULT 0.0000, -- Стоимость только энергии
    total_idle_amount   NUMERIC(12,4) NOT NULL DEFAULT 0.0000, -- Стоимость простоя (пеня)
    total_final_amount  NUMERIC(12,4) NOT NULL DEFAULT 0.0000, -- Итоговая сумма к списанию

    -- Системные поля для аудита и паттерна Optimistic Locking
    version             BIGINT NOT NULL DEFAULT 0,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE charging_invoices ADD CONSTRAINT fk_invoices_sessions FOREIGN KEY (session_id) REFERENCES charging_sessions(id);

-- Индекс для быстрого поиска активных транзакций пользователя (проверка баланса / личный кабинет)
CREATE INDEX IF NOT EXISTS idx_transactions_user_status ON charging_invoices (user_id, status);

-- Индекс для аналитического модуля (выручка станций, объемы прокачки по времени)
CREATE INDEX IF NOT EXISTS idx_transactions_analytics ON charging_sessions (connector_id, started_at)
WHERE status = 'COMPLETED';

