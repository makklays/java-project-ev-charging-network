-- V4__create_charging_stations_table.sql
-- Migration #4: create table charging_stations

-- Creating table 'charging_stations' for storing main token data
CREATE TABLE IF NOT EXISTS charging_stations (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,

    -- Гео-координаты для поиска по радиусу и отображения на карте в приложении
    latitude NUMERIC(9,6) NOT NULL,
    longitude NUMERIC(9,6) NOT NULL,

    -- Общая физическая мощность заправки (ограничение сети ДТЭК для балансировки)
    max_power_kw NUMERIC(6,2) NOT NULL,

    -- Статус работоспособности всей локации (например: ONLINE, OFFLINE, UNDER_MAINTENANCE)
    status VARCHAR(50) NOT NULL DEFAULT 'ONLINE',

    -- Системные поля для аудита и паттерна Optimistic Locking
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индекс для высоконагруженного гео-поиска по радиусу (Haversine formula / B-Tree)
CREATE INDEX IF NOT EXISTS idx_charging_stations_geo ON charging_stations (latitude, longitude);

