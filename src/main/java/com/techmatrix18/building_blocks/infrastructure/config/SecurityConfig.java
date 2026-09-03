package com.techmatrix18.building_blocks.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SecurityConfig
 * Конфигурация криптографической защиты и шифрования персональных данных пользователей EV-сети.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 03.09.2026
 */

@Configuration
public class SecurityConfig {

    /**
     * Создает и регистрирует бин PasswordEncoder в контексте Spring Boot.
     * Использует стойкий алгоритм шифрования BCrypt (стандарт индустрии).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

