package com.techmatrix18.charging_tariff.application.port.in;

import com.techmatrix18.charging_tariff.application.command.CreateTariffCommand;

/**
 * CreateTariffUseCase
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface CreateTariffUseCase {
    void createTariff(CreateTariffCommand command);
}

