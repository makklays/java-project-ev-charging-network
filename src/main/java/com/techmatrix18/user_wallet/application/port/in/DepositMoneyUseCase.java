package com.techmatrix18.user_wallet.application.port.in;

import com.techmatrix18.user_wallet.application.command.DepositMoneyCommand;

/**
 * DepositMoneyUseCase
 * Входной порт для пополнения баланса кошелька пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface DepositMoneyUseCase {
    void depositMoney(DepositMoneyCommand command);
}

