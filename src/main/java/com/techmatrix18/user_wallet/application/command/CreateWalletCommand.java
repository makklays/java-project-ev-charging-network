package com.techmatrix18.user_wallet.application.command;

import java.util.Objects;

/**
 * CreateWalletCommand
 * Команда для автоматического создания нового кошелька при регистрации пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 26.08.2026
 */

public record CreateWalletCommand(
        Long userId
) {
    public CreateWalletCommand {
        Objects.requireNonNull(userId, "User ID is required to create a wallet");
    }
}

