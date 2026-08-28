package com.techmatrix18.ledger_audit_log.insrastructure.http;

import com.techmatrix18.ledger_audit_log.application.command.ArchiveOldLedgerEntriesCommand;
import com.techmatrix18.ledger_audit_log.application.command.CreateLedgerCorrectionCommand;
import com.techmatrix18.ledger_audit_log.application.command.LogLedgerEntryCommand;
import com.techmatrix18.ledger_audit_log.application.port.in.ArchiveOldLedgerEntriesUseCase;
import com.techmatrix18.ledger_audit_log.application.port.in.CreateLedgerCorrectionUseCase;
import com.techmatrix18.ledger_audit_log.application.port.in.LogLedgerEntryUseCase;
import com.techmatrix18.ledger_audit_log.application.port.out.LedgerAuditLogRepository;
import com.techmatrix18.ledger_audit_log.domain.LedgerAuditLog;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * LedgerAuditLogController
 * HTTP-адаптер REST API для финансового аудита и ручного сторнирования балансов
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

@RestController
@RequestMapping("/api/v1/ledger-audit-logs")
public class LedgerAuditLogController {

    private final LedgerAuditLogRepository ledgerRepository;
    private final LogLedgerEntryUseCase logLedgerEntryUseCase;
    private final CreateLedgerCorrectionUseCase createLedgerCorrectionUseCase;
    private final ArchiveOldLedgerEntriesUseCase archiveOldLedgerEntriesUseCase;

    public LedgerAuditLogController(
            LedgerAuditLogRepository ledgerRepository,
            LogLedgerEntryUseCase logLedgerEntryUseCase,
            CreateLedgerCorrectionUseCase createLedgerCorrectionUseCase,
            ArchiveOldLedgerEntriesUseCase archiveOldLedgerEntriesUseCase
    ) {
        this.ledgerRepository = ledgerRepository;
        this.logLedgerEntryUseCase = logLedgerEntryUseCase;
        this.createLedgerCorrectionUseCase = createLedgerCorrectionUseCase;
        this.archiveOldLedgerEntriesUseCase = archiveOldLedgerEntriesUseCase;
    }

    // Получение конкретной финансовой проводки по ID для детальной проверки аудитором
    @GetMapping("/{id}")
    public ResponseEntity<LedgerAuditLog> getLedgerEntryById(@PathVariable Long id) {
        LedgerAuditLog logEntry = ledgerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Ledger audit entry not found with ID: " + id));
        return ResponseEntity.ok(logEntry);
    }

    // Системный триггер: Прямая фиксация стандартной Append-Only проводки (вызывается биллинг-модулями)
    @PostMapping
    public ResponseEntity<Void> logLedgerEntry(@Valid @RequestBody LogLedgerEntryCommand command) {
        logLedgerEntryUseCase.logLedgerEntry(command);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // HTTP 201 Created
    }

    // Административный триггер: Ручное сторнирование (исправление) баланса без изменения старой истории
    @PostMapping("/corrections")
    public ResponseEntity<Void> createLedgerCorrection(@Valid @RequestBody CreateLedgerCorrectionCommand command) {
        // Вызов бизнес-сервиса исправления баланса
        createLedgerCorrectionUseCase.createLedgerCorrection(command);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // HTTP 201 Created
    }

    // Обслуживающий триггер БД: Выгрузка и очистка пачек исторических данных в холодное хранилище AWS S3
    @PostMapping("/maintenance/archive")
    public ResponseEntity<Void> archiveOldEntries(@Valid @RequestBody ArchiveOldLedgerEntriesCommand command) {
        // Вызов обслуживающего сервиса архивации БД
        archiveOldLedgerEntriesUseCase.archiveOldEntries(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}

