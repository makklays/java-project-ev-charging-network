package com.techmatrix18.connector.application.port.in;

import com.techmatrix18.connector.application.command.UpdateConnectorSpecsCommand;

/**
 * UpdateConnectorSpecsUseCase
 * Входной порт для обновления технических характеристик (спецификаций) зарядного кабеля
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface UpdateConnectorSpecsUseCase {
    void updateConnectorSpecs(UpdateConnectorSpecsCommand command);
}

