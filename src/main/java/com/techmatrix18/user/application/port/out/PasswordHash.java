package com.techmatrix18.user.application.port.out;

/**
 * PasswordHashPort
 * Выходной порт для хеширования и проверки паролей в доменном слое
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public interface PasswordHash {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}

