package com.techmatrix18.ledger_audit_log.application.service;

import com.techmatrix18.ledger_audit_log.application.command.LogLedgerEntryCommand;
import com.techmatrix18.ledger_audit_log.application.port.in.LogLedgerEntryUseCase;
import com.techmatrix18.ledger_audit_log.application.port.out.LedgerAuditLogRepository;
import com.techmatrix18.ledger_audit_log.domain.LedgerAuditLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LogLedgerEntryService
 * Реализация бизнес-логики фиксации аудиторской финансовой проводки
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

@Service
public class LogLedgerEntryService implements LogLedgerEntryUseCase {

    private final LedgerAuditLogRepository ledgerRepository;

    public LogLedgerEntryService(LedgerAuditLogRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    @Override
    @Transactional // Выполняется строго в рамках текущей финансовой транзакции СУБД
    public void logLedgerEntry(LogLedgerEntryCommand command) {

        // Инициализируем чистую, иммутабельную доменную модель BillingLedgerAuditLog.
        // Конструктор домена проверит физические инварианты (отсутствие пустой строки типа операции и т.д.).
        LedgerAuditLog auditLogEntry = new LedgerAuditLog(
                command.userId(),
                command.chargingInvoiceId(),
                command.operationType(),
                command.amount(),
                command.walletBalanceSnapshot(),
                command.historicalPricePerKwh(),
                command.historicalTariffName(),
                command.deltaKwh(),
                command.deltaMinutes(),
                command.totalMeterKwh(),
                command.auditComment()
        );

        // Сохраняем запись в СУБД. Происходит исключительно операция SQL INSERT.
        ledgerRepository.save(auditLogEntry);
    }
}

