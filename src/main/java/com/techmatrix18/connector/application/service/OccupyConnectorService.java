package com.techmatrix18.connector.application.service;

import com.techmatrix18.connector.application.command.OccupyConnectorCommand;
import com.techmatrix18.connector.application.port.in.OccupyConnectorUseCase;
import com.techmatrix18.connector.application.port.out.ConnectorRepository;
import com.techmatrix18.connector.domain.Connector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OccupyConnectorService
 * Реализация бизнес-логики фиксации старта протекания тока через кабель (перевод в статус CHARGING)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class OccupyConnectorService implements OccupyConnectorUseCase {

    private final ConnectorRepository connectorRepository;

    public OccupyConnectorService(ConnectorRepository connectorRepository) {
        this.connectorRepository = connectorRepository;
    }

    @Override
    @Transactional // Атомарно изолирует транзакцию изменения статуса и активирует Optimistic Locking
    public void occupyConnector(OccupyConnectorCommand command) {
        // Извлекаем доменную модель коннектора из репозитория через выходной порт
        Connector connector = connectorRepository.findById(command.connectorId())
                .orElseThrow(() -> new IllegalArgumentException("Connector not found with ID: " + command.connectorId()));

        // Делегируем изменение состояния внутренней бизнес-логике Rich Model
        // Метод переведет статус в ConnectorStatus.CHARGING и обновит временную метку updatedAt
        connector.occupy();

        // Сохраняем обновленный объект в базу данных через выходной порт
        connectorRepository.save(connector);
    }
}

