package com.techmatrix18.user.application.command;

import com.techmatrix18.user.domain.BaseRole;
import java.util.Objects;

/**
 * ChangeUserRoleCommand
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record ChangeUserRoleCommand(
        Long userId,
        BaseRole newRole
) {
    public ChangeUserRoleCommand {
        Objects.requireNonNull(userId, "User ID is required for changing role");
        Objects.requireNonNull(newRole, "New role cannot be null");
    }
}

