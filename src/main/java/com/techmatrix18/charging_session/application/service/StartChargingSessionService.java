package com.techmatrix18.charging_session.application.service;

import com.techmatrix18.charging_session.application.command.StartChargingSessionCommand;
import com.techmatrix18.charging_session.application.port.in.StartChargingSessionUseCase;
import com.techmatrix18.charging_session.application.port.out.ChargingSessionRepository;
import com.techmatrix18.charging_session.domain.ChargingSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * StartChargingSessionService
 * Сервис запуска зарядной сессии. Создает запись с начальными показаниями счетчика.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class StartChargingSessionService implements StartChargingSessionUseCase {

    private final ChargingSessionRepository sessionRepository;

    public StartChargingSessionService(ChargingSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional // Открывает транзакцию для атомарной вставки записи в charging_sessions
    public void startSession(StartChargingSessionCommand command) {
        // Инициализируем Rich Domain Model через бизнес-конструктор.
        // Он установит статус IN_PROGRESS, зафиксирует started_at и обнулит денежные счетчики.
        ChargingSession newSession = new ChargingSession(
                command.userId(),
                command.connectorId(),
                command.startMeterValue()
        );

        // Сохраняем сессию. База данных сгенерирует уникальный ID (BIGSERIAL).
        sessionRepository.save(newSession);
    }
}

