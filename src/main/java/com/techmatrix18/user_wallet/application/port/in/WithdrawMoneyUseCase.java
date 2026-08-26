package com.techmatrix18.user_wallet.application.port.in;

import com.techmatrix18.user_wallet.application.command.WithdrawMoneyCommand;

/**
 * WithdrawMoneyUseCase
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 26.08.2026
 */

public interface WithdrawMoneyUseCase {
    void withdrawMoney(WithdrawMoneyCommand command);
}

