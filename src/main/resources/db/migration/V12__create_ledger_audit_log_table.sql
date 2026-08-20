-- V12__create_billing_ledger_audit_log_table.sql
-- Migration #12: create table billing_ledger_audit_log

-- Неизменяемый (Append-Only) журнал финансовых проводок для аудита биллинга EV Charging network
CREATE TABLE IF NOT EXISTS billing_ledger_audit_log (
    id UUID PRIMARY KEY,

    -- Связь с биллинговой транзакцией (счетом)
    billing_transaction_id UUID NOT NULL REFERENCES billing_charging_transactions(id) ON DELETE RESTRICT,
    -- Пользователь, у которого изменился баланс
    user_id UUID NOT NULL REFERENCES user_wallets(user_id) ON DELETE RESTRICT,

    -- Тип финансовой проводки (например: PERIODIC_ENERGY_DEBIT, IDLE_FEE_DEBIT, INVOICE_FINAL_SETTLEMENT)
    operation_type VARCHAR(50) NOT NULL,

    -- Конкретная сумма этой проводки (всегда дебет или кредит, например: -12.5500 или +200.0000)
    amount NUMERIC(12,4) NOT NULL,

    -- Фиксация баланса кошелька ПОСЛЕ этой операции (главный инструмент аудитора)
    wallet_balance_snapshot NUMERIC(12,4) NOT NULL,

    -- цена, которая действовала именно в эту секунду
    historical_price_per_kwh NUMERIC(10,4),
    -- название зоны (NIGHT/PEAK)
    historical_tariff_name VARCHAR(50),

    -- Сколько кВт*ч было потреблено именно в рамках этого тика (дельта для сверки с физикой зарядок)
    delta_kwh NUMERIC(12,3) NOT NULL DEFAULT 0.000,  -- Сколько кВт*ч потреблено за этот тик (0.000 для простоя)
    delta_minutes INT NOT NULL DEFAULT 0,            -- Сколько минут простоя зафиксировано (0 для энергии)
    total_meter_kwh NUMERIC(12,3) NOT NULL,          -- Абсолютное значение счетчика станции в момент тика

    -- Временная метка проводки в UTC
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Идентификатор системы или компонента, совершившего запись (например: 'BILLING_ENGINE_V1')
    created_by VARCHAR(100) NOT NULL DEFAULT 'BILLING_ENGINE',

    -- Человекочитаемый комментарий или метаданные (например: "Time window: 23:00-23:01. Tariff: NIGHT")
    audit_comment VARCHAR(255)
);

-- Индексы для мгновенной сборки истории списаний и проверки баланса
CREATE INDEX IF NOT EXISTS idx_ledger_audit_tx_link ON billing_ledger_audit_log (billing_transaction_id, created_at ASC);
CREATE INDEX IF NOT EXISTS idx_ledger_audit_user_timeline ON billing_ledger_audit_log (user_id, created_at DESC);

