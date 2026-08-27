package com.techmatrix18.evse_point.infrastructure.db;

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

    // Внедряем Spring Data репозиторий через конструктор
    public EvsePointRepositoryAdapter(JpaEvsePointRepository repository) {
        this.repository = repository;
    }

    @Override
    public EvsePoint save(EvsePoint evsePoint) {
        // Конвертируем чистый Домен в JPA Entity
        EvsePointEntity entity = EvsePointEntity.fromDomain(evsePoint);

        // Сохраняем в базу данных через Spring Data
        EvsePointEntity savedEntity = repository.save(entity);

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

