package com.techmatrix18.charging_tariff.application.command;

import java.util.Objects;

/**
 * DeleteTariffCommand
 * Команда для удаления тарифной зоны из биллинговой системы сети электрозаправок
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record DeleteTariffCommand(
        Long tariffId
) {
    public DeleteTariffCommand {
        Objects.requireNonNull(tariffId, "Tariff ID is required for deletion");
    }
}

