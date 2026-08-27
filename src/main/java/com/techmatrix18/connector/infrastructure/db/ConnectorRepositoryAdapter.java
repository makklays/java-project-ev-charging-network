package com.techmatrix18.connector.infrastructure.db;

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

    // Внедряем Spring Data репозиторий через конструктор
    public ConnectorRepositoryAdapter(JpaConnectorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Connector save(Connector connector) {
        // Конвертируем чистый Домен в JPA Entity
        ConnectorEntity entity = ConnectorEntity.fromDomain(connector);

        // Сохраняем в базу данных через Spring Data
        ConnectorEntity savedEntity = repository.save(entity);

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

