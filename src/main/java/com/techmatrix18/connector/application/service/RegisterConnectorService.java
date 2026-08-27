package com.techmatrix18.connector.application.service;

import com.techmatrix18.connector.application.command.RegisterConnectorCommand;
import com.techmatrix18.connector.application.port.in.RegisterConnectorUseCase;
import com.techmatrix18.connector.application.port.out.ConnectorRepository;
import com.techmatrix18.connector.domain.Connector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RegisterConnectorService
 * Реализация бизнес-логики регистрации и монтажа нового коннектора
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class RegisterConnectorService implements RegisterConnectorUseCase {

    private final ConnectorRepository connectorRepository;

    public RegisterConnectorService(ConnectorRepository connectorRepository) {
        this.connectorRepository = connectorRepository;
    }

    @Override
    @Transactional // Гарантирует атомарность операции в рамках транзакции СУБД
    public void registerConnector(RegisterConnectorCommand command) {
        // 1. Проверка бизнес-инварианта и уникального ограничения таблицы (uk_evse_connector_number)
        if (connectorRepository.existsByEvseIdAndConnectorNumber(command.evseId(), command.connectorNumber())) {
            throw new IllegalStateException(String.format(
                    "Connector with number %d already exists on EVSE point ID: %d",
                    command.connectorNumber(), command.evseId()));
        }

        // 2. Создаем чистую доменную модель коннектора
        // Конструктор домена автоматически установит статус AVAILABLE и версию 0L
        Connector newConnector = new Connector(
                command.evseId(),
                command.connectorNumber(),
                command.connectorType(),
                command.currentType(),
                command.maxPowerKw()
        );

        // 3. Сохраняем объект в базу данных через выходной порт
        connectorRepository.save(newConnector);
    }
}

