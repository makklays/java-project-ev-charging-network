package com.techmatrix18.charging_session.domain;

/**
 * ChargingSessionStatus
 * Жизненный цикл зарядной сессии (транзакции)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public enum ChargingSessionStatus {
    IN_PROGRESS,
    COMPLETED,
    SUSPENDED,
    FAILED
}

