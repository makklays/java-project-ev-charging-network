package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.ChangeUserRoleCommand;
import com.techmatrix18.user.domain.BaseRole;
import jakarta.validation.constraints.NotNull;

/**
 * ChangeUserRoleRequest
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public record ChangeUserRoleRequest(
        @NotNull(message = "User ID is required to change role") Long userId,
        @NotNull(message = "New role must be specified") BaseRole newRole
) {
    public ChangeUserRoleCommand toCommand() {
        return new ChangeUserRoleCommand(this.userId, this.newRole);
    }
}

