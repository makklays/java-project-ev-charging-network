package com.techmatrix18.user_wallet.application.port.in;

import com.techmatrix18.user_wallet.application.command.CreateWalletCommand;

/**
 * CreateWalletUseCase
 * Входной порт для создания нового кошелька пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 26.08.2026
 */

public interface CreateWalletUseCase {
    void createWallet(CreateWalletCommand command);
}

