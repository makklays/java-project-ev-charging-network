package com.techmatrix18.evse_point.infrastructure.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.building_blocks.infrastructure.db.JpaOutboxEventRepository;
import com.techmatrix18.building_blocks.infrastructure.db.OutboxEventEntity;
import com.techmatrix18.evse_point.application.port.out.EvsePointRepository;
import com.techmatrix18.evse_point.domain.EvsePoint;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * EvsePointRepositoryAdapter
 * Выходной адаптер для управления постоянным хранением точек зарядки (EVSE) в JPA
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Component
public class EvsePointRepositoryAdapter implements EvsePointRepository {

    private final JpaEvsePointRepository repository;
    private final JpaOutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    // Внедряем Spring Data репозиторий через конструктор
    public EvsePointRepositoryAdapter(JpaEvsePointRepository repository,
                                      JpaOutboxEventRepository outboxRepository,
                                      ObjectMapper objectMapper) {
        this.repository = repository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public EvsePoint save(EvsePoint evsePoint) {
        // Конвертируем чистый Домен в JPA Entity
        EvsePointEntity entity = EvsePointEntity.fromDomain(evsePoint);

        // Сохраняем в базу данных через Spring Data
        EvsePointEntity savedEntity = repository.save(entity);

        // [OUTBOX EVENT]: Цикл перебора и сохранения доменных событий в таблицу Outbox
        for (Object event : evsePoint.getDomainEvents()) {
            try {
                // Превращаем доменное событие в строку JSON
                String jsonPayload = objectMapper.writeValueAsString(event);

                // Заполняем системную сущность Outbox
                OutboxEventEntity outboxEntry = new OutboxEventEntity();
                outboxEntry.setAggregateId(evsePoint.getId().toString()); // ID физического порта
                outboxEntry.setAggregateType("EVSE_POINT"); // Авто-топик в Kafka: evse-point-events
                outboxEntry.setEventType(event.getClass().getSimpleName()); // Имя класса (например, EvseStatusChangedEvent)
                outboxEntry.setPayload(jsonPayload);

                // Сохраняем запись в рамках текущей бизнес-транзакции СУБД
                outboxRepository.save(outboxEntry);

            } catch (Exception e) {
                throw new RuntimeException("Ошибка автоматической записи события порта EVSE в Outbox", e);
            }
        }

        // [OUTBOX EVENT]: Стираем отработанные события из памяти доменного объекта
        evsePoint.clearDomainEvents();

        // Возвращаем обратно чистую доменную модель
        return savedEntity.toDomain();
    }

    @Override
    public Optional<EvsePoint> findById(Long id) {
        // Извлекаем Entity из БД и, если она найдена, маппим в Домен
        return repository.findById(id)
                .map(EvsePointEntity::toDomain);
    }

    @Override
    public boolean existsByStationIdAndEvseNumber(Long stationId, Integer evseNumber) {
        // Перенаправляем проверку бизнес-инварианта напрямую в базу данных
        return repository.existsByStationIdAndEvseNumber(stationId, evseNumber);
    }
}

