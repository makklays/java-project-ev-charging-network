package com.techmatrix18.ledger_audit_log.application.port.in;

import com.techmatrix18.ledger_audit_log.application.command.LogLedgerEntryCommand;

/**
 * LogLedgerEntryUseCase
 * Входной порт для добавления новой записи в неизменяемую бухгалтерскую книгу (Ledger)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

public interface LogLedgerEntryUseCase {
    void logLedgerEntry(LogLedgerEntryCommand command);
}

