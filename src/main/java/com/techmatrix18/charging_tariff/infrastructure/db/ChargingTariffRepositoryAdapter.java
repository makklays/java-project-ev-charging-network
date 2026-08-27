package com.techmatrix18.charging_tariff.infrastructure.db;

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

    // Внедряем Spring Data репозиторий через конструктор
    public ChargingTariffRepositoryAdapter(JpaChargingTariffRepository repository) {
        this.repository = repository;
    }

    @Override
    public ChargingTariff save(ChargingTariff tariff) {
        // Конвертируем чистый Домен в JPA Entity
        ChargingTariffEntity entity = ChargingTariffEntity.fromDomain(tariff);

        // Сохраняем в базу данных через Spring Data
        ChargingTariffEntity savedEntity = repository.save(entity);

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

