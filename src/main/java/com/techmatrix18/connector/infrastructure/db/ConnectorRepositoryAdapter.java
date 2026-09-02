package com.techmatrix18.connector.infrastructure.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.building_blocks.infrastructure.db.JpaOutboxEventRepository;
import com.techmatrix18.building_blocks.infrastructure.db.OutboxEventEntity;
import com.techmatrix18.connector.application.port.out.ConnectorRepository;
import com.techmatrix18.connector.domain.Connector;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * ConnectorRepositoryAdapter
 * Выходной адаптер для управления постоянным хранением коннекторов (кабелей) в JPA
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Component
public class ConnectorRepositoryAdapter implements ConnectorRepository {

    private final JpaConnectorRepository repository;
    private final JpaOutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    // Внедряем Spring Data репозиторий через конструктор
    public ConnectorRepositoryAdapter(JpaConnectorRepository repository,
                                      JpaOutboxEventRepository outboxRepository,
                                      ObjectMapper objectMapper) {
        this.repository = repository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Connector save(Connector connector) {
        // Конвертируем чистый Домен в JPA Entity
        ConnectorEntity entity = ConnectorEntity.fromDomain(connector);

        // Сохраняем в базу данных через Spring Data
        ConnectorEntity savedEntity = repository.save(entity);

        // [OUTBOX EVENT]: Цикл перебора и сохранения доменных событий в таблицу Outbox
        for (Object event : connector.getDomainEvents()) {
            try {
                // Превращаем доменное событие в строку JSON
                String jsonPayload = objectMapper.writeValueAsString(event);

                // Заполняем системную сущность Outbox
                OutboxEventEntity outboxEntry = new OutboxEventEntity();
                outboxEntry.setAggregateId(connector.getId().toString()); // ID физического коннектора
                outboxEntry.setAggregateType("CONNECTOR"); // Сформирует Kafka топик: connector-events
                outboxEntry.setEventType(event.getClass().getSimpleName()); // Имя класса (например, ConnectorStatusChangedEvent)
                outboxEntry.setPayload(jsonPayload);

                // Сохраняем в единую таблицу outbox_events в рамках текущей транзакции
                outboxRepository.save(outboxEntry);

            } catch (Exception e) {
                throw new RuntimeException("Ошибка автоматической записи события кабеля-коннектора в Outbox", e);
            }
        }

        // [OUTBOX EVENT]: Стираем отработанные события из памяти доменного объекта
        connector.clearDomainEvents();

        // Возвращаем обратно чистую доменную модель
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Connector> findById(Long id) {
        // Извлекаем Entity из БД и, если она найдена, маппим в Домен
        return repository.findById(id).map(ConnectorEntity::toDomain);
    }

    @Override
    public boolean existsByEvseIdAndConnectorNumber(Long evseId, Integer connectorNumber) {
        // Перенаправляем проверку бизнес-инварианта напрямую в базу данных
        return repository.existsByEvseIdAndConnectorNumber(evseId, connectorNumber);
    }
}

