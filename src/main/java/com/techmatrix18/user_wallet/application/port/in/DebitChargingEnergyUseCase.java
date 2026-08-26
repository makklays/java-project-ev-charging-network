package com.techmatrix18.user_wallet.application.port.in;

import com.techmatrix18.user_wallet.application.command.DebitChargingEnergyCommand;

/**
 * DebitChargingEnergyUseCase
 * Входной порт для периодического списания денег за потребленную энергию во время зарядки
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface DebitChargingEnergyUseCase {
    void debitChargingEnergy(DebitChargingEnergyCommand command);
}

