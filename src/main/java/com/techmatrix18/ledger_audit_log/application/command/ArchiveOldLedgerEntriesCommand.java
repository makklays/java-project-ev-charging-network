package com.techmatrix18.ledger_audit_log.application.command;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * ArchiveOldLedgerEntriesCommand
 * Команда для порционной архивации и очистки старых записей финансового журнала (партиционирование/выгрузка)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

public record ArchiveOldLedgerEntriesCommand(
        ZonedDateTime olderThan,
        Integer batchSize
) {
    public ArchiveOldLedgerEntriesCommand {
        Objects.requireNonNull(olderThan, "Archivation cut-off date cannot be null");
        Objects.requireNonNull(batchSize, "Batch size configuration cannot be null");

        // Бизнес-проверка: Запрещено архивировать свежие финансовые данные (например, за текущий год)
        if (olderThan.isAfter(ZonedDateTime.now().minusYears(1))) {
            throw new IllegalArgumentException("For audit compliance, you cannot archive financial ledger entries newer than 1 year");
        }

        // Защита от перегрузки памяти (OOM) и слишком тяжелых транзакций в PostgreSQL
        if (batchSize <= 0 || batchSize > 50000) {
            throw new IllegalArgumentException("Batch size must stay between 1 and 50,000 rows per transaction");
        }
    }
}

