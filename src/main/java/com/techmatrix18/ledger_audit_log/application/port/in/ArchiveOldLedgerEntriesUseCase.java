package com.techmatrix18.ledger_audit_log.application.port.in;

import com.techmatrix18.ledger_audit_log.application.command.ArchiveOldLedgerEntriesCommand;

/**
 * ArchiveOldLedgerEntriesUseCase
 * Входной порт для регламентного обслуживания и архивации старых проводок книги аудита
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

public interface ArchiveOldLedgerEntriesUseCase {
    void archiveOldEntries(ArchiveOldLedgerEntriesCommand command);
}

