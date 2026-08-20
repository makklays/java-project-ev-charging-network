-- V5__create_evse_points_table.sql
-- Migration #5: create table evse_points

-- Creating table 'evse_points' for storing main token data
CREATE TABLE IF NOT EXISTS evse_points (
    id UUID PRIMARY KEY,
    station_id UUID NOT NULL REFERENCES charging_stations(id) ON DELETE CASCADE,
    evse_number INT NOT NULL, -- порядковый номер точки на конкретной станции (1, 2, 3...)

    -- Текущий статус точки (OCPP стандарт: AVAILABLE, PREPARING, CHARGING, SUSPENDED_EV, FAULTED)
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',

    -- Уникальный физический идентификатор OCPP (если станция передает его в ином формате)
    ocpp_evse_id INT NOT NULL,

    -- Системные поля для аудита и паттерна Optimistic Locking
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Ограничение: номер EVSE в рамках одной станции должен быть уникальным
    CONSTRAINT uk_station_evse_number UNIQUE (station_id, evse_number)
);

-- Индекс для мгновенной фильтрации свободных/занятых мест на карте приложения YASNO
CREATE INDEX IF NOT EXISTS idx_evse_points_station_status ON evse_points (station_id, status);

