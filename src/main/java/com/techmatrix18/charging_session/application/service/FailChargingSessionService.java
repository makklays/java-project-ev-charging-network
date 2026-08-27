package com.techmatrix18.charging_session.application.service;

import com.techmatrix18.charging_session.application.command.FailChargingSessionCommand;
import com.techmatrix18.charging_session.application.port.in.FailChargingSessionUseCase;
import com.techmatrix18.charging_session.application.port.out.ChargingSessionRepository;
import com.techmatrix18.charging_session.domain.ChargingSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FailChargingSessionService
 * Вызывается, если контроллер железной станции прислал аварийный код сбоя во время протекания тока.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class FailChargingSessionService implements FailChargingSessionUseCase {

    private final ChargingSessionRepository sessionRepository;

    public FailChargingSessionService(ChargingSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public void failSession(FailChargingSessionCommand command) {
        ChargingSession session = sessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new IllegalArgumentException("Charging session not found with ID: " + command.sessionId()));

        // 1. Доменная Rich Model выставляет статус FAILED и фиксирует finishedAt время
        session.fail();

        // 2. Сохраняем аварийную сессию
        sessionRepository.save(session);
    }
}

