package com.techmatrix18.connector.application.port.in;

import com.techmatrix18.connector.application.command.RegisterConnectorCommand;

/**
 * RegisterConnectorUseCase
 * Входной порт для монтажа и регистрации нового зарядного кабеля на точке EVSE
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface RegisterConnectorUseCase {
    void registerConnector(RegisterConnectorCommand command);
}

