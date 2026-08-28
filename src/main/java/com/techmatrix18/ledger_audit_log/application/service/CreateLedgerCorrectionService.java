package com.techmatrix18.ledger_audit_log.application.service;

import com.techmatrix18.ledger_audit_log.application.command.CreateLedgerCorrectionCommand;
import com.techmatrix18.ledger_audit_log.application.port.in.CreateLedgerCorrectionUseCase;
import com.techmatrix18.ledger_audit_log.application.port.out.LedgerAuditLogRepository;
import com.techmatrix18.ledger_audit_log.domain.LedgerAuditLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * CreateLedgerCorrectionService
 * Реализация бизнес-логики внесения ручных финансовых корректировок администратором
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

@Service
public class CreateLedgerCorrectionService implements CreateLedgerCorrectionUseCase {

    private final LedgerAuditLogRepository ledgerRepository;

    public CreateLedgerCorrectionService(LedgerAuditLogRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    @Override
    @Transactional // Гарантирует атомарность операции. Ошибка при создании лога откатит всю операцию.
    public void createLedgerCorrection(CreateLedgerCorrectionCommand command) {

        // Инициализируем новую неизменяемую доменную модель BillingLedgerAuditLog
        // Указываем специфический тип операции "ADMIN_MANUAL_CORRECTION"
        // Физические дельты кВт*ч и минут выставляем в 0, так как это чисто финансовое исправление
        LedgerAuditLog correctionEntry = new LedgerAuditLog(
                command.userId(),
                command.chargingInvoiceId(),
                "ADMIN_MANUAL_CORRECTION", // Жестко фиксируем тип операции в логе
                command.amount(),
                command.walletBalanceSnapshot(),
                null,                      // Историческая цена за кВт*ч отсутствует
                null,                      // Историческое имя тарифа отсутствует
                BigDecimal.ZERO,           // Дельта кВт*ч = 0 (нет физического потребления)
                0,                         // Дельта минут простоя = 0
                BigDecimal.ZERO,           // Текущий счетчик станции = 0
                command.auditComment()     // Обязательное текстовое обоснование для аудиторов
        );

        // Делаем исключительно SQL INSERT в базу данных через выходной порт репозитория
        ledgerRepository.save(correctionEntry);

        //  Интеграция со смежными контекстами (Event-Driven Architecture):
        // После фиксации проводки здесь можно опубликовать событие LedgerCorrectionCreatedEvent в Kafka,
        // чтобы модуль кошельков (UserWallet) синхронизировал баланс пользователя на значение command.amount().
    }
}

