package com.techmatrix18.charging_invoice.application.port.in;

import com.techmatrix18.charging_invoice.application.command.MarkInvoiceAsFailedCommand;

/**
 * MarkInvoiceAsFailedUseCase
 * Входной порт для фиксации отклонения платежа СУБД/банком
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface MarkInvoiceAsFailedUseCase {
    void markAsFailed(MarkInvoiceAsFailedCommand command);
}

