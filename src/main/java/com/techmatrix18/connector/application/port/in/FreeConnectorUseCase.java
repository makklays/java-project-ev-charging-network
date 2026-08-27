package com.techmatrix18.connector.application.port.in;

import com.techmatrix18.connector.application.command.FreeConnectorCommand;

/**
 * FreeConnectorUseCase
 * Входной порт для обработки события отключения кабеля и возврата коннектора в статус Available
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface FreeConnectorUseCase {
    void freeConnector(FreeConnectorCommand command);
}

