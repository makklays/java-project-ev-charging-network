package com.techmatrix18.ledger_audit_log.insrastructure.db;

import com.techmatrix18.ledger_audit_log.application.port.out.LedgerColdStorage;
import com.techmatrix18.ledger_audit_log.domain.LedgerAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * AwsS3LedgerColdStorageAdapter
 * Исправленная заглушка для интеграции с холодным хранилищем AWS S3.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 03.09.2026
 */

@Component
public class AwsS3LedgerColdStorageAdapter implements LedgerColdStorage {

    private static final Logger log = LoggerFactory.getLogger(AwsS3LedgerColdStorageAdapter.class);

    @Override
    public void uploadToColdStorage(List<LedgerAuditLog> entries) { // <--- ИМЯ МЕТОДА СИНХРОНИЗИРОВАНО
        log.info("[AWS S3 COLD STORAGE STUB] Запрос на выгрузку {} исторических проводок принят.", entries.size());

        for (LedgerAuditLog entry : entries) {
            log.debug("Выгрузка в S3 проводки ID: {} | Сумма: {}", entry.getId(), entry.getAmount());
        }

        log.info("[AWS S3 COLD STORAGE STUB] Пакет данных успешно передан в облачный архив.");
    }
}

