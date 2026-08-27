package com.techmatrix18.evse_point.domain;

/**
 * EvseStatus
 * Технологические статусы точки зарядки в соответствии со спецификацией OCPP
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public enum EvseStatus {
    AVAILABLE,      // Свободна
    PREPARING,      // Подготовка (автомобиль подключен, идет авторизация)
    CHARGING,       // Идет зарядка (протекает ток)
    SUSPENDED_EV,   // Зарядка приостановлена электромобилем
    FAULTED         // Аппаратная ошибка / Критический сбой компонента
}

