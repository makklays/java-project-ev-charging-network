package com.techmatrix18.ledger_audit_log.application.port.out;

import com.techmatrix18.ledger_audit_log.domain.LedgerAuditLog;
import java.util.List;

/**
 * LedgerColdStoragePort
 * Выходной порт для отправки исторических финансовых данных в холодное облачное хранилище
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

public interface LedgerColdStorage {
    void uploadToColdStorage(List<LedgerAuditLog> entries);
}

