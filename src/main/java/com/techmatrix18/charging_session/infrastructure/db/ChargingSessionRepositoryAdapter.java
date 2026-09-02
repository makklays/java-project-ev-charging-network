package com.techmatrix18.charging_session.infrastructure.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.building_blocks.infrastructure.db.JpaOutboxEventRepository;
import com.techmatrix18.building_blocks.infrastructure.db.OutboxEventEntity;
import com.techmatrix18.charging_session.application.port.out.ChargingSessionRepository;
import com.techmatrix18.charging_session.domain.ChargingSession;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * ChargingSessionRepositoryAdapter
 * Выходной адаптер для управления постоянным хранением зарядных сессий в JPA
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Component
public class ChargingSessionRepositoryAdapter implements ChargingSessionRepository {

    private final JpaChargingSessionRepository repository;
    private final JpaOutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    // Внедряем Spring Data репозиторий через конструктор
    public ChargingSessionRepositoryAdapter(JpaChargingSessionRepository repository,
                                            JpaOutboxEventRepository outboxRepository,
                                            ObjectMapper objectMapper) {
        this.repository = repository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public ChargingSession save(ChargingSession session) {
        // Конвертируем чистый Домен в JPA Entity
        ChargingSessionEntity entity = ChargingSessionEntity.fromDomain(session);

        // Сохраняем в базу данных через Spring Data
        ChargingSessionEntity savedEntity = repository.save(entity);

        // [OUTBOX EVENT]: Цикл перебора и сохранения доменных событий в таблицу Outbox
        for (Object event : session.getDomainEvents()) {
            try {
                // Превращаем доменное событие в строку JSON
                String jsonPayload = objectMapper.writeValueAsString(event);

                // Заполняем системную сущность Outbox
                OutboxEventEntity outboxEntry = new OutboxEventEntity();
                outboxEntry.setAggregateId(session.getId().toString()); // ID сессии зарядки
                outboxEntry.setAggregateType("CHARGING_SESSION"); // Сформирует Kafka топик: charging-session-events
                outboxEntry.setEventType(event.getClass().getSimpleName()); // Имя класса (например, ChargingSessionStartedEvent)
                outboxEntry.setPayload(jsonPayload);

                // Сохраняем в единую таблицу outbox_events в рамках текущей транзакции
                outboxRepository.save(outboxEntry);

            } catch (Exception e) {
                throw new RuntimeException("Ошибка автоматической записи операционного события сессии в Outbox", e);
            }
        }

        // [OUTBOX EVENT]: Стираем отработанные события из памяти доменного объекта
        session.clearDomainEvents();

        // Возвращаем обратно чистую доменную модель с обновленным ID и version
        return savedEntity.toDomain();
    }

    @Override
    public Optional<ChargingSession> findById(Long id) {
        // Извлекаем Entity из БД и, если она найдена, маппим в Домен
        return repository.findById(id)
                .map(ChargingSessionEntity::toDomain);
    }
}

