package com.techmatrix18.user.application.port.out;

import com.techmatrix18.user.domain.User;
import java.util.Optional;

/**
 * UserRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}

