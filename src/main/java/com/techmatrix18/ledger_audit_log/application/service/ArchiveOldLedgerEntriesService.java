package com.techmatrix18.ledger_audit_log.application.service;

import com.techmatrix18.ledger_audit_log.application.command.ArchiveOldLedgerEntriesCommand;
import com.techmatrix18.ledger_audit_log.application.port.in.ArchiveOldLedgerEntriesUseCase;
import com.techmatrix18.ledger_audit_log.application.port.out.LedgerAuditLogRepository;
import com.techmatrix18.ledger_audit_log.application.port.out.LedgerColdStorage;
import com.techmatrix18.ledger_audit_log.domain.LedgerAuditLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ArchiveOldLedgerEntriesService
 * Сервис порционной выгрузки и очистки устаревших аудит-логов (Паттерн Data Archival / Purging)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

@Service
public class ArchiveOldLedgerEntriesService implements ArchiveOldLedgerEntriesUseCase {

    private final LedgerAuditLogRepository ledgerRepository;
    private final LedgerColdStorage coldStorage; // Инфраструктурный порт для S3/MinIO/ClickHouse

    public ArchiveOldLedgerEntriesService(
            LedgerAuditLogRepository ledgerRepository,
            LedgerColdStorage coldStorage
    ) {
        this.ledgerRepository = ledgerRepository;
        this.coldStorage = coldStorage;
    }

    @Override
    @Transactional // Оборачивает каждую итерацию пачки в изолированную транзакцию СУБД
    public void archiveOldEntries(ArchiveOldLedgerEntriesCommand command) {
        // Извлекаем порцию старых записей согласно лимиту batchSize, установленному в команде
        List<LedgerAuditLog> oldEntries = ledgerRepository.findOldEntries(
                command.olderThan(),
                command.batchSize()
        );

        // Если старых записей за этот период больше нет — останавливаем выполнение
        if (oldEntries.isEmpty()) {
            return;
        }

        // Отправляем пачку сущностей через выходной порт в холодное хранилище (S3/CSV/Parquet)
        // Если выгрузка оборвется по сети, транзакция откатится и данные из PostgreSQL не удалятся
        coldStorage.uploadToColdStorage(oldEntries);

        // Собираем ID успешно заархивированных строк
        List<Long> idsToDelete = oldEntries.stream()
                .map(LedgerAuditLog::getId)
                .collect(Collectors.toList());

        // Очищаем операционную базу данных от выгруженных строк, освобождая дисковое пространство
        ledgerRepository.deleteEntriesByIds(idsToDelete);
    }
}

