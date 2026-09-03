package com.techmatrix18.charging_station.infrastructure.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.building_blocks.infrastructure.db.JpaOutboxEventRepository;
import com.techmatrix18.building_blocks.infrastructure.db.OutboxEventEntity;
import com.techmatrix18.charging_station.application.port.out.ChargingStationRepository;
import com.techmatrix18.charging_station.domain.ChargingStation;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

/**
 * ChargingStationRepositoryAdapter
 * Выходной адаптер для управления постоянным хранением станций в PostgreSQL через JPA
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Component
public class ChargingStationRepositoryAdapter implements ChargingStationRepository {

    private final JpaChargingStationRepository repository;
    private final JpaOutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public ChargingStationRepositoryAdapter(JpaChargingStationRepository repository,
                                            JpaOutboxEventRepository outboxRepository,
                                            ObjectMapper objectMapper) {
        this.repository = repository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public ChargingStation save(ChargingStation station) {
        // Конвертируем доменную модель в JPA сущность
        ChargingStationEntity entity = ChargingStationEntity.fromDomain(station);

        // Сохраняем в СУБД с автоматическим контролем версий и инкрементом ID
        ChargingStationEntity savedEntity = repository.save(entity);

        // [OUTBOX EVENT]: Цикл перебора и сохранения доменных событий в таблицу Outbox
        for (Object event : station.getDomainEvents()) {
            try {
                // Превращаем доменное событие в строку JSON
                String jsonPayload = objectMapper.writeValueAsString(event);

                // Заполняем системную сущность Outbox
                OutboxEventEntity outboxEntry = new OutboxEventEntity();
                String stationIdStr = String.valueOf(station.getId());
                outboxEntry.setAggregateId(UUID.nameUUIDFromBytes(stationIdStr.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
                outboxEntry.setAggregateType("CHARGING_STATION"); // Сформирует Kafka топик: charging-station-events
                outboxEntry.setEventType(event.getClass().getSimpleName()); // Имя класса (например, StationLifecycleChangedEvent)
                outboxEntry.setPayload(jsonPayload);

                // Сохраняем в единую таблицу outbox_events в рамках текущей транзакции
                outboxRepository.save(outboxEntry);

            } catch (Exception e) {
                throw new RuntimeException("Ошибка автоматической записи события зарядной станции в Outbox", e);
            }
        }

        // [OUTBOX EVENT]: Стираем отработанные события из памяти доменного объекта
        station.clearDomainEvents();

        // Возвращаем обратно чистую доменную модель
        return savedEntity.toDomain();
    }

    @Override
    public Optional<ChargingStation> findById(Long id) {
        // Извлекаем запись из БД и прозрачно маппим её в доменный Rich Model
        return repository.findById(id).map(ChargingStationEntity::toDomain);
    }
}

