package com.techmatrix18.charging_invoice.application.port.in;

import com.techmatrix18.charging_invoice.application.command.IssueInvoiceCommand;

/**
 * IssueInvoiceUseCase
 * Входной порт для первичного формирования и выставления бухгалтерского счета
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface IssueInvoiceUseCase {
    void issueInvoice(IssueInvoiceCommand command);
}

