-- V6__create_connectors_table.sql
-- Migration #6: create table connectors

-- Creating table 'connectors' for storing physical plug configurations
-- Таблица физических зарядных пистолетов / кабелей
CREATE TABLE IF NOT EXISTS connectors (
    id                  BIGSERIAL PRIMARY KEY,
    evse_id             BIGINT NOT NULL REFERENCES evse_points(id) ON DELETE RESTRICT,

    -- Порядковый номер кабеля на конкретном парковочном месте (обычно 1 или 2)
    connector_number    INT NOT NULL,

    -- Международный стандарт разъема (например: CCS2, CHADEMO, TYPE2, GBT_DC)
    connector_type      VARCHAR(50) NOT NULL,

    -- Тип выдаваемого тока (AC - переменный, DC - постоянный быстрый)
    current_type        VARCHAR(10) NOT NULL,

    -- Максимальная конструктивная мощность конкретного пистолета (кВт)
    max_power_kw        NUMERIC(6,2) NOT NULL,

    -- Текущий статус конкретного кабеля (AVAILABLE, CHARGING, FAULTED)
    status              VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',

    -- Системные поля для аудита и паттерна Optimistic Locking
    version             BIGINT NOT NULL DEFAULT 0,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Номер коннектора должен быть уникальным в рамках одного EVSE
    CONSTRAINT uk_evse_connector_number UNIQUE (evse_id, connector_number)
);

-- Индекс для быстрой выборки структуры портов при просмотре конкретной станции в приложении
CREATE INDEX IF NOT EXISTS idx_connectors_evse ON connectors (evse_id);

