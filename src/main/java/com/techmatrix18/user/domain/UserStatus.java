package com.techmatrix18.user.domain;

/**
 * UserStatus
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

public enum UserStatus {
    PENDING,   // Ожидает подтверждения (например, после регистрации)
    ACTIVE,    // Активен (результат вызова команды activate)
    BLOCKED,   // Заблокирован (результат вызова команды block)
    DELETED,    // Мягко удален (результат вызова команды delete)

    DRIVER,
    PASSENGER,
    VIP
}

