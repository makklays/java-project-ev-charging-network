package com.techmatrix18.ledger_audit_log.application.port.in;

import com.techmatrix18.ledger_audit_log.application.command.CreateLedgerCorrectionCommand;

/**
 * CreateLedgerCorrectionUseCase
 * Входной порт для создания ручной корректирующей проводки в финансовом журнале
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

public interface CreateLedgerCorrectionUseCase {
    void createLedgerCorrection(CreateLedgerCorrectionCommand command);
}

