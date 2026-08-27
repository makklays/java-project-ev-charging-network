package com.techmatrix18.connector.application.service;

import com.techmatrix18.connector.application.command.UpdateConnectorSpecsCommand;
import com.techmatrix18.connector.application.port.in.UpdateConnectorSpecsUseCase;
import com.techmatrix18.connector.application.port.out.ConnectorRepository;
import com.techmatrix18.connector.domain.Connector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpdateConnectorSpecsService
 * Реализация бизнес-логики технической модернизации кабеля оборудования
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class UpdateConnectorSpecsService implements UpdateConnectorSpecsUseCase {

    private final ConnectorRepository connectorRepository;

    public UpdateConnectorSpecsService(ConnectorRepository connectorRepository) {
        this.connectorRepository = connectorRepository;
    }

    @Override
    @Transactional // Гарантирует атомарность операции и контроль версии записи СУБД для предотвращения Race Conditions
    public void updateConnectorSpecs(UpdateConnectorSpecsCommand command) {
        // 1. Извлекаем доменную модель коннектора из базы данных через выходной порт
        Connector connector = connectorRepository.findById(command.connectorId())
                .orElseThrow(() -> new IllegalArgumentException("Connector not found with ID: " + command.connectorId()));

        // 2. Вызываем доменный бизнес-метод изменения характеристик Rich Model (его код приведен ниже)
        connector.updateSpecs(command.connectorType(), command.maxPowerKw());

        // 3. Сохраняем измененное состояние в базу данных через выходной порт
        connectorRepository.save(connector);
    }
}

