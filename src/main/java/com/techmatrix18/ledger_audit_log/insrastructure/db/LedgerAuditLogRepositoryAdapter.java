package com.techmatrix18.ledger_audit_log.insrastructure.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.building_blocks.infrastructure.db.JpaOutboxEventRepository;
import com.techmatrix18.building_blocks.infrastructure.db.OutboxEventEntity;
import com.techmatrix18.ledger_audit_log.application.port.out.LedgerAuditLogRepository;
import com.techmatrix18.ledger_audit_log.domain.LedgerAuditLog;
import com.techmatrix18.user_wallet.application.port.out.BillingLedgerAuditRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * LedgerAuditLogRepositoryAdapter
 * Выходной адаптер для управления постоянным хранением логов финансового аудита в PostgreSQL через JPA
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

@Component
public class LedgerAuditLogRepositoryAdapter implements LedgerAuditLogRepository, BillingLedgerAuditRepository {

    private final JpaBillingLedgerRepository repository;
    private final JpaOutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public LedgerAuditLogRepositoryAdapter(JpaBillingLedgerRepository repository, JpaOutboxEventRepository outboxRepository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public LedgerAuditLog save(LedgerAuditLog ledgerLog) {
        // Конвертируем чистую иммутабельную доменную модель в JPA Entity
        LedgerAuditLogEntity entity = LedgerAuditLogEntity.fromDomain(ledgerLog);

        // Выполняем строго операцию SQL INSERT в базу данных
        LedgerAuditLogEntity savedEntity = repository.save(entity);

        // [OUTBOX EVENT]: Универсальный цикл перебора доменных событий проводки
        for (Object event : ledgerLog.getDomainEvents()) {
            try {
                String jsonPayload = objectMapper.writeValueAsString(event);

                OutboxEventEntity outboxEntry = new OutboxEventEntity();
                String ledgerIdStr = String.valueOf(ledgerLog.getId());
                outboxEntry.setAggregateId(UUID.nameUUIDFromBytes(ledgerIdStr.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
                outboxEntry.setAggregateType("LEDGER_AUDIT");             // Kafka топик: ledger-audit-events
                outboxEntry.setEventType(event.getClass().getSimpleName()); // Имя класса: LedgerEntryLoggedEvent
                outboxEntry.setPayload(jsonPayload);

                // Сохраняем в таблицу outbox_events через чистый Spring Data репозиторий
                outboxRepository.save(outboxEntry);

            } catch (Exception e) {
                throw new RuntimeException("Ошибка автоматической записи финансовой проводки в Outbox", e);
            }
        }

        // [OUTBOX EVENT]: Стираем отработанные события из памяти доменного объекта
        ledgerLog.clearDomainEvents();

        // Возвращаем доменную модель со сгенерированным СУБД первичным ключом ID
        return savedEntity.toDomain();
    }

    @Override
    public Optional<LedgerAuditLog> findById(Long id) {
        return repository.findById(id).map(LedgerAuditLogEntity::toDomain);
    }

    @Override
    public List<LedgerAuditLog> findOldEntries(java.time.ZonedDateTime cutOffDate, int limit) {
        List<LedgerAuditLogEntity> entities = repository.findOldEntries(cutOffDate, limit);

        return entities.stream()
                .map(LedgerAuditLogEntity::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deleteEntriesByIds(java.util.List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        repository.deleteEntriesByIds(ids);
    }

    @Override
    public void logOperation(Long userId, Long referenceId, String operationType,
                             java.math.BigDecimal amount, java.math.BigDecimal currentBalance, String comment) {

        // Вызываем наш новый, чистый и удобный конструктор из 6 параметров!
        LedgerAuditLog domainLog = new LedgerAuditLog(
                userId,          // 1. Long userId
                referenceId,     // 2. Long chargingInvoiceId
                operationType,   // 3. String operationType
                amount,          // 4. BigDecimal amount
                currentBalance,  // 5. BigDecimal walletBalanceSnapshot
                comment          // 6. String auditComment
        );

        // Отправляем на сохранение (в базу и Outbox)
        this.save(domainLog);
    }
}

