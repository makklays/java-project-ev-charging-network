package com.techmatrix18.charging_session.application.service;

import com.techmatrix18.charging_session.application.command.CompleteChargingSessionCommand;
import com.techmatrix18.charging_session.application.port.in.CompleteChargingSessionUseCase;
import com.techmatrix18.charging_session.application.port.out.ChargingSessionRepository;
import com.techmatrix18.charging_session.domain.ChargingSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CompleteChargingSessionService
 * Сервис финализации сессии. Фиксирует статус COMPLETED и время окончания finished_at.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class CompleteChargingSessionService implements CompleteChargingSessionUseCase {

    private final ChargingSessionRepository sessionRepository;

    public CompleteChargingSessionService(ChargingSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public void completeSession(CompleteChargingSessionCommand command) {
        ChargingSession session = sessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new IllegalArgumentException("Charging session not found with ID: " + command.sessionId()));

        // Переводим статус домена в COMPLETED и проставляем время финиша finishedAt
        session.complete();

        // Записываем финальное состояние сессии в базу данных
        sessionRepository.save(session);

        //  Стык контекстов через события (EDA):
        // Здесь публикуется событие SessionCompletedEvent в Kafka топик "billing-events".
        // Модуль UserWallet (кошельки) перехватит его и выполнит Use Case финального расчета инвойса
        // (SettleFinalInvoiceUseCase), который мы писали ранее, списав итоговую сумму с баланса пользователя.
    }
}

