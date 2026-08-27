package com.techmatrix18.connector.application.port.in;

import com.techmatrix18.connector.application.command.ReportConnectorFaultCommand;

/**
 * ReportConnectorFaultUseCase
 * Входной порт для обработки аварийных сигналов и фиксации поломок зарядного кабеля
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface ReportConnectorFaultUseCase {
    void reportConnectorFault(ReportConnectorFaultCommand command);
}

