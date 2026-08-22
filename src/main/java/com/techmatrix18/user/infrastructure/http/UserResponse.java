package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.domain.User;

/**
 * UserResponse
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

public record UserResponse(Long id, String email, String nickname, String status) {
    public static UserResponse fromDomain(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname(), user.getStatus().name());
    }
}

