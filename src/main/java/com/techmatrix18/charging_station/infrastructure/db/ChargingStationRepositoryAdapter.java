package com.techmatrix18.charging_station.infrastructure.db;

import com.techmatrix18.charging_station.application.port.out.ChargingStationRepository;
import com.techmatrix18.charging_station.domain.ChargingStation;
import org.springframework.stereotype.Component;
import java.util.Optional;

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

    public ChargingStationRepositoryAdapter(JpaChargingStationRepository repository) {
        this.repository = repository;
    }

    @Override
    public ChargingStation save(ChargingStation station) {
        // Конвертируем доменную модель в JPA сущность
        ChargingStationEntity entity = ChargingStationEntity.fromDomain(station);

        // Сохраняем в СУБД с автоматическим контролем версий и инкрементом ID
        ChargingStationEntity savedEntity = repository.save(entity);

        // Возвращаем обратно чистую доменную модель
        return savedEntity.toDomain();
    }

    @Override
    public Optional<ChargingStation> findById(Long id) {
        // Извлекаем запись из БД и прозрачно маппим её в доменный Rich Model
        return repository.findById(id).map(ChargingStationEntity::toDomain);
    }
}

