package com.techmatrix18.charging_invoice.infrastructure.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.building_blocks.infrastructure.db.JpaOutboxEventRepository;
import com.techmatrix18.building_blocks.infrastructure.db.OutboxEventEntity;
import com.techmatrix18.charging_invoice.application.port.out.ChargingInvoiceRepository;
import com.techmatrix18.charging_invoice.domain.ChargingInvoice;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * ChargingInvoiceRepositoryAdapter
 * Выходной адаптер для управления постоянным хранением бухгалтерских счетов в JPA
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Component
public class ChargingInvoiceRepositoryAdapter implements ChargingInvoiceRepository {

    private final JpaChargingInvoiceRepository repository;
    private final JpaOutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    // Внедряем Spring Data репозиторий через конструктор
    public ChargingInvoiceRepositoryAdapter(JpaChargingInvoiceRepository repository,
                                            JpaOutboxEventRepository outboxRepository,
                                            ObjectMapper objectMapper) {
        this.repository = repository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public ChargingInvoice save(ChargingInvoice invoice) {
        // Конвертируем чистый Домен в JPA Entity
        ChargingInvoiceEntity entity = ChargingInvoiceEntity.fromDomain(invoice);

        // Сохраняем в базу данных через Spring Data
        ChargingInvoiceEntity savedEntity = repository.save(entity);

        // [OUTBOX EVENT]: Цикл перебора и сохранения доменных событий в таблицу Outbox
        for (Object event : invoice.getDomainEvents()) {
            try {
                // Превращаем доменное событие в строку JSON
                String jsonPayload = objectMapper.writeValueAsString(event);

                // Заполняем системную сущность Outbox
                OutboxEventEntity outboxEntry = new OutboxEventEntity();
                outboxEntry.setAggregateId(invoice.getId().toString()); // ID инвойса/счета
                outboxEntry.setAggregateType("CHARGING_INVOICE"); // Сформирует Kafka топик: charging-invoice-events
                outboxEntry.setEventType(event.getClass().getSimpleName()); // Имя класса (например, InvoicePaidEvent)
                outboxEntry.setPayload(jsonPayload);

                // Сохраняем в единую таблицу outbox_events в рамках текущей транзакции
                outboxRepository.save(outboxEntry);

            } catch (Exception e) {
                throw new RuntimeException("Ошибка автоматической записи финансового события инвойса в Outbox", e);
            }
        }

        // [OUTBOX EVENT]: Стираем отработанные события из памяти доменного объекта
        invoice.clearDomainEvents();

        // Возвращаем обратно чистую доменную модель с обновленным ID и version
        return savedEntity.toDomain();
    }

    @Override
    public Optional<ChargingInvoice> findById(Long id) {
        // Извлекаем Entity из БД и, если она найдена, маппим в Домен
        return repository.findById(id).map(ChargingInvoiceEntity::toDomain);
    }
}

