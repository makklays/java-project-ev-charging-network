package com.techmatrix18.connector.application.service;

import com.techmatrix18.connector.application.command.ReportConnectorFaultCommand;
import com.techmatrix18.connector.application.port.in.ReportConnectorFaultUseCase;
import com.techmatrix18.connector.application.port.out.ConnectorRepository;
import com.techmatrix18.connector.domain.Connector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ReportConnectorFaultService
 * Реализация бизнес-логики фиксации аппаратного сбоя кабеля (перевод в статус FAULTED)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class ReportConnectorFaultService implements ReportConnectorFaultUseCase {

    private final ConnectorRepository connectorRepository;

    public ReportConnectorFaultService(ConnectorRepository connectorRepository) {
        this.connectorRepository = connectorRepository;
    }

    @Override
    @Transactional // Атомарно изолирует транзакцию изменения статуса и активирует Optimistic Locking
    public void reportConnectorFault(ReportConnectorFaultCommand command) {
        // Извлекаем доменную модель коннектора из репозитория через выходной порт
        Connector connector = connectorRepository.findById(command.connectorId())
                .orElseThrow(() -> new IllegalArgumentException("Connector not found with ID: " + command.connectorId()));

        // Делегируем изменение состояния внутренней бизнес-логике Rich Model
        // Метод переведет статус в ConnectorStatus.FAULTED и обновит временную метку updatedAt
        connector.reportFault();

        // Сохраняем обновленный объект в базу данных через выходной порт
        connectorRepository.save(connector);

        //  Рекомендация для продакшена:
        // Здесь можно отправить уведомление (команду/событие command.errorReason()) в систему техподдержки,
        // чтобы автоматически завести тикет на ремонт неисправного кабеля.
    }
}

