-- V9__create_idempotency_records_table.sql
-- Migration #9: create table idempotency_records

-- Таблица записей идемпотентности (Сетевой щит на входе в систему)
CREATE TABLE idempotency_records (
    idempotency_key VARCHAR(255) PRIMARY KEY, -- Уникальный UUID-токен, пришедший от фронтенда
    request_payload_hash VARCHAR(64) NOT NULL, -- SHA-256 хэш тела запроса для защиты от подмены данных
    status VARCHAR(20) NOT NULL CHECK (status IN ('STARTED', 'PROCESSING', 'COMPLETED', 'FAILED')),
    response_code INT,                        -- HTTP-статус, который мы вернули (например, 200 или 400)
    response_body TEXT,                       -- Закешированный JSON-ответ для мгновенного возврата дубликатам
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE DEFAULT (CURRENT_TIMESTAMP + INTERVAL '24 hours') -- Ключ активен только сутки
);

-- Индекс для высокопроизводительной очистки просроченных ключей
CREATE INDEX idx_idempotency_expires_at ON idempotency_records(expires_at);

