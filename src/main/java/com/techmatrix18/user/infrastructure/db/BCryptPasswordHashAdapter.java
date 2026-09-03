package com.techmatrix18.user.infrastructure.db;

import com.techmatrix18.user.application.port.out.PasswordHash;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * BCryptPasswordHashAdapter
 * Синхронизированный адаптер хэширования паролей под ваш доменный интерфейс.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 03.09.2026
 */

@Component
public class BCryptPasswordHashAdapter implements PasswordHash {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordHashAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) { // <--- ИМЯ ИЗМЕНЕНО НА encode
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

