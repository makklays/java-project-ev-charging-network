package com.techmatrix18.connector.application.port.in;

import com.techmatrix18.connector.application.command.OccupyConnectorCommand;

/**
 * OccupyConnectorUseCase
 * Входной порт для обработки события замыкания реле и перевода коннектора в статус CHARGING
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface OccupyConnectorUseCase {
    void occupyConnector(OccupyConnectorCommand command);
}

