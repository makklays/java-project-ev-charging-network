package com.techmatrix18.connector.application.service;

import com.techmatrix18.connector.application.command.FreeConnectorCommand;
import com.techmatrix18.connector.application.port.in.FreeConnectorUseCase;
import com.techmatrix18.connector.application.port.out.ConnectorRepository;
import com.techmatrix18.connector.domain.Connector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FreeConnectorService
 * Реализация бизнес-логики освобождения зарядного кабеля (перевод в статус AVAILABLE)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class FreeConnectorService implements FreeConnectorUseCase {

    private final ConnectorRepository connectorRepository;

    public FreeConnectorService(ConnectorRepository connectorRepository) {
        this.connectorRepository = connectorRepository;
    }

    @Override
    @Transactional // Атомарно изолирует транзакцию изменения статуса и активирует Optimistic Locking
    public void freeConnector(FreeConnectorCommand command) {
        // Извлекаем доменную модель коннектора из репозитория через выходной порт
        Connector connector = connectorRepository.findById(command.connectorId())
                .orElseThrow(() -> new IllegalArgumentException("Connector not found with ID: " + command.connectorId()));

        // Делегируем изменение состояния внутренней бизнес-логике Rich Model
        // Метод вернет статус в ConnectorStatus.AVAILABLE и обновит временную метку updatedAt
        connector.makeAvailable();

        // Сохраняем обновленный объект в базу данных через выходной порт
        connectorRepository.save(connector);
    }
}

