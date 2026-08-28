package com.techmatrix18.ledger_audit_log.application.port.out;

import com.techmatrix18.ledger_audit_log.domain.LedgerAuditLog;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * BillingLedgerAuditLogRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

public interface LedgerAuditLogRepository {

    LedgerAuditLog save(LedgerAuditLog ledgerLog);

    Optional<LedgerAuditLog> findById(Long id);

    // Находит пачку старых записей для выгрузки
    List<LedgerAuditLog> findOldEntries(ZonedDateTime cutOffDate, int limit);

    // Удаляет пачку записей по их ID в рамках одной транзакции
    void deleteEntriesByIds(List<Long> ids);
}

