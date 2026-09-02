package com.techmatrix18.charging_tariff.infrastructure.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmatrix18.building_blocks.infrastructure.db.JpaOutboxEventRepository;
import com.techmatrix18.building_blocks.infrastructure.db.OutboxEventEntity;
import com.techmatrix18.charging_tariff.application.port.out.ChargingTariffRepository;
import com.techmatrix18.charging_tariff.domain.ChargingTariff;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * ChargingTariffRepositoryAdapter
 * Выходной адаптер для управления постоянным хранением тарифов биллинга в JPA
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Component
public class ChargingTariffRepositoryAdapter implements ChargingTariffRepository {

    private final JpaChargingTariffRepository repository;
    private final JpaOutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    // Внедряем Spring Data репозиторий через конструктор
    public ChargingTariffRepositoryAdapter(JpaChargingTariffRepository repository,
                                           JpaOutboxEventRepository outboxRepository,
                                           ObjectMapper objectMapper) {
        this.repository = repository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public ChargingTariff save(ChargingTariff tariff) {
        // Конвертируем чистый Домен в JPA Entity
        ChargingTariffEntity entity = ChargingTariffEntity.fromDomain(tariff);

        // Сохраняем в базу данных через Spring Data
        ChargingTariffEntity savedEntity = repository.save(entity);

        // [OUTBOX EVENT]: Цикл перебора и сохранения доменных событий в таблицу Outbox
        for (Object event : tariff.getDomainEvents()) {
            try {
                // Превращаем доменное событие в строку JSON
                String jsonPayload = objectMapper.writeValueAsString(event);

                // Заполняем системную сущность Outbox
                OutboxEventEntity outboxEntry = new OutboxEventEntity();
                outboxEntry.setAggregateId(tariff.getId().toString()); // ID тарифной зоны
                outboxEntry.setAggregateType("CHARGING_TARIFF"); // Сформирует Kafka топик: charging-tariff-events
                outboxEntry.setEventType(event.getClass().getSimpleName()); // Имя класса (например, TariffPriceChangedEvent)
                outboxEntry.setPayload(jsonPayload);

                // Сохраняем в единую таблицу outbox_events в рамках текущей транзакции
                outboxRepository.save(outboxEntry);

            } catch (Exception e) {
                throw new RuntimeException("Ошибка автоматической записи события тарифа в Outbox", e);
            }
        }

        // [OUTBOX EVENT]: Стираем отработанные события из памяти доменного объекта
        tariff.clearDomainEvents();

        // Возвращаем обратно чистую доменную модель
        return savedEntity.toDomain();
    }

    @Override
    public Optional<ChargingTariff> findById(Long id) {
        // Извлекаем Entity из БД и, если она найдена, маппим в Домен
        return repository.findById(id)
                .map(ChargingTariffEntity::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        // Выполняем физическое удаление тарифа из СУБД
        repository.deleteById(id);
    }
}

