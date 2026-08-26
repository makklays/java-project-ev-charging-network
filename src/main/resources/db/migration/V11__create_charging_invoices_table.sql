-- V11__create_charging_invoices_table.sql
-- Migration #11: create table charging_invoices

-- Таблица финансового итога (официальный счет / инвойс за зарядку)
CREATE TABLE IF NOT EXISTS charging_invoices (
    id                      BIGSERIAL PRIMARY KEY,

    -- Жесткая связь с физической сессией (один счет на одну сессию)
    session_id              BIGINT NOT NULL REFERENCES charging_sessions(id) ON DELETE RESTRICT,
    user_id                 BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,

    -- Уникальный номер счета для бухгалтерии (например: INV-2026-08-00123)
    invoice_number          VARCHAR(50) NOT NULL,

    -- Статус счета (ISSUED - выставлен, PAID - оплачен, FAILED - ошибка списания, DISPUTED)
    status                  VARCHAR(50) NOT NULL DEFAULT 'ISSUED',

    -- [ВРЕМЕННЫЕ ПОКАЗАТЕЛИ] Длительность процессов в минутах
    charging_duration_minutes       INT NOT NULL DEFAULT 0, -- Чистое время протекания тока в авто
    idle_minutes                    INT NOT NULL DEFAULT 0, -- Время платного простоя места (оккупация кабеля)
    total_session_duration_minutes  INT NOT NULL DEFAULT 0, -- Общее время нахождения на заправке (от старта до стопа)

    -- [ОБЪЕМЫ УСЛУГ] Количественные показатели
    consumed_kwh            NUMERIC(12,3) NOT NULL DEFAULT 0.000,  -- Количество купленной электроэнергии в кВт*ч

    -- [ФИНАНСЫ] Финальные агрегированные показатели, зафиксированные на момент закрытия
    energy_amount           NUMERIC(12,4) NOT NULL DEFAULT 0.0000, -- Итоговая стоимость чистой энергии (без НДС)
    idle_amount             NUMERIC(12,4) NOT NULL DEFAULT 0.0000, -- Итоговая стоимость простоя места (без НДС)
    vat_amount              NUMERIC(12,4) NOT NULL DEFAULT 0.0000, -- Сумма НДС (в Украине 20%) от суммы энергии и простоя
    final_amount_with_vat   NUMERIC(12,4) NOT NULL DEFAULT 0.0000, -- Финальная сумма к списанию с кошелька (включая НДС)

    -- Временные метки формирования и оплаты счета
    issued_at               TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    paid_at                 TIMESTAMP WITH TIME ZONE,

    -- Системное поле для паттерна Optimistic Locking
    version                 BIGINT NOT NULL DEFAULT 0,

    -- запрещает создавать два инвойса для одной и той же конкретной сессии СЕССИЯ-ААА
    CONSTRAINT uk_invoices_session UNIQUE (session_id),
    CONSTRAINT uk_invoices_number UNIQUE (invoice_number)
);

-- Индекс для быстрого поиска инвойсов пользователя
CREATE INDEX IF NOT EXISTS idx_charging_invoices_user ON charging_invoices (user_id, status);

