package com.techmatrix18.charging_session.infrastructure.db;

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

    // Внедряем Spring Data репозиторий через конструктор
    public ChargingSessionRepositoryAdapter(JpaChargingSessionRepository repository) {
        this.repository = repository;
    }

    @Override
    public ChargingSession save(ChargingSession session) {
        // 1. Конвертируем чистый Домен в JPA Entity
        ChargingSessionEntity entity = ChargingSessionEntity.fromDomain(session);

        // 2. Сохраняем в базу данных через Spring Data
        ChargingSessionEntity savedEntity = repository.save(entity);

        // 3. Возвращаем обратно чистую доменную модель с обновленным ID и version
        return savedEntity.toDomain();
    }

    @Override
    public Optional<ChargingSession> findById(Long id) {
        // Извлекаем Entity из БД и, если она найдена, маппим в Домен
        return repository.findById(id)
                .map(ChargingSessionEntity::toDomain);
    }
}

