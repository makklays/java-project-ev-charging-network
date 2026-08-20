-- V10__create_outbox_events_table.sql
-- Migration #10: create table outbox_events

-- Таблица для реализации паттерна Transactional Outbox
CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    aggregate_type VARCHAR(50) NOT NULL,     -- Тип агрегата, породившего событие (например, 'PAYMENT')
    aggregate_id UUID NOT NULL,              -- Идентификатор сущности бизнес-логики (ID транзакции)
    event_type VARCHAR(100) NOT NULL,        -- Название события ('PAYMENT_SUCCESS', 'PAYMENT_FAILED')
    payload TEXT NOT NULL,                   -- Сериализованный в JSON технический payload события
    status VARCHAR(20) NOT NULL,             -- Статус отправки в Kafka/RabbitMQ: 'PENDING', 'PROCESSED', 'FAILED'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP WITH TIME ZONE    -- Таймстамп, когда фоновый шедулер успешно доставил сообщение в брокер
);

-- Индекс для фонового процесса (Scheduler/Debezium), который постоянно выбирает неотправленные сообщения
CREATE INDEX idx_outbox_events_status_created ON outbox_events(status, created_at);

