package com.techmatrix18.charging_session.application.service;

import com.techmatrix18.charging_session.application.command.ApplySessionIdleFeeCommand;
import com.techmatrix18.charging_session.application.port.in.ApplySessionIdleFeeUseCase;
import com.techmatrix18.charging_session.application.port.out.ChargingSessionRepository;
import com.techmatrix18.charging_session.domain.ChargingSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * ApplySessionIdleFeeService
 * Вызывается Кроном или IoT-триггером, когда машина заряжена, но водитель не уезжает.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class ApplySessionIdleFeeService implements ApplySessionIdleFeeUseCase {

    private final ChargingSessionRepository sessionRepository;

    public ApplySessionIdleFeeService(ChargingSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public void applyIdleFee(ApplySessionIdleFeeCommand command) {
        ChargingSession session = sessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new IllegalArgumentException("Charging session not found with ID: " + command.sessionId()));

        // Вычисляем финансовую сумму штрафа за этот тик времени (минуты * тариф)
        BigDecimal idleAmountDelta = command.idlePricePerMin().multiply(BigDecimal.valueOf(command.idleMinutesDelta()));

        // Делегируем начисление пени доменной модели. Она прибавит её к totalIdleAmount и пересчитает итог.
        session.applyIdleFee(idleAmountDelta);

        // Сохраняем обновленный инвойс сессии в СУБД
        sessionRepository.save(session);
    }
}

