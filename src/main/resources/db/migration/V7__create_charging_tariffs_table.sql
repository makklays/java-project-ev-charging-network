-- V7__create_charging_tariffs_table.sql
-- Migration #7: create table charging_tariffs

-- Creating table 'charging_tariffs' for flexible, time-zoned pricing grids
-- Таблица гибких, привязанных ко времени суток тарифов для каждого типа коннектора
CREATE TABLE IF NOT EXISTS charging_tariffs (
    id                  BIGSERIAL PRIMARY KEY,
    connector_id        BIGINT NOT NULL REFERENCES connectors(id) ON DELETE RESTRICT,

    -- Название тарифной зоны (например: NIGHT, PEAK, STANDARD)
    zone_name           VARCHAR(100) NOT NULL,

    -- Временные границы действия тарифа (сравнение идет по времени суток)
    start_time          TIME NOT NULL,  -- Время начала действия тарифа (например, 23:00:00 для ночного)
    end_time            TIME NOT NULL,  -- Время окончания действия тарифа (например, 07:00:00 для ночного)

    -- Стоимость 1 кВт*ч электроэнергии (NUMERIC исключает потерю округления)
    price_per_kwh       NUMERIC(10,4) NOT NULL,

    -- Тариф за минуту простоя (включается, если машина заряжена, но занимает место)
    idle_price_per_min  NUMERIC(10,4) NOT NULL DEFAULT 0.0000,

    -- Системные поля для аудита и паттерна Optimistic Locking
    version             BIGINT NOT NULL DEFAULT 0,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индекс для мгновенного мэтчинга тарифа по времени в реальном времени (Billing Engine)
CREATE INDEX IF NOT EXISTS idx_tariffs_connector_time ON charging_tariffs (connector_id, start_time, end_time);

