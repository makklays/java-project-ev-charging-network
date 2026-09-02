package com.techmatrix18.ledger_audit_log.insrastructure.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.building_blocks.infrastructure.db.JpaOutboxEventRepository;
import com.techmatrix18.building_blocks.infrastructure.db.OutboxEventEntity;
import com.techmatrix18.ledger_audit_log.application.port.out.LedgerAuditLogRepository;
import com.techmatrix18.ledger_audit_log.domain.LedgerAuditLog;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

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
public class LedgerAuditLogRepositoryAdapter implements LedgerAuditLogRepository {

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
                outboxEntry.setAggregateId(ledgerLog.getId().toString()); // ID проводки
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
        // Чтение лога для финтех-аудита или генерации выписки пользователя
        return repository.findById(id).map(LedgerAuditLogEntity::toDomain);
    }

    @Override
    public List<LedgerAuditLog> findOldEntries(java.time.ZonedDateTime cutOffDate, int limit) {
        // Вызов метода Spring Data репозитория (имя полностью совпадает)
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
        // Вызов метода пакетного удаления Spring Data репозитория (имя полностью совпадает)
        repository.deleteEntriesByIds(ids);
    }
}

