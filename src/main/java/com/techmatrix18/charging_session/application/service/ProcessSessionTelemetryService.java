package com.techmatrix18.charging_session.application.service;

import com.techmatrix18.charging_session.application.command.ProcessSessionTelemetryCommand;
import com.techmatrix18.charging_session.application.port.in.ProcessSessionTelemetryUseCase;
import com.techmatrix18.charging_session.application.port.out.ChargingSessionRepository;
import com.techmatrix18.charging_session.domain.ChargingSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProcessSessionTelemetryService
 * Высоконагруженный сервис, вызываемый Kafka-воркером при обновлении счетчиков энергии.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class ProcessSessionTelemetryService implements ProcessSessionTelemetryUseCase {

    private final ChargingSessionRepository sessionRepository;

    public ProcessSessionTelemetryService(ChargingSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional // Важно для паттерна Optimistic Locking: Hibernate проверит версию перед фиксацией
    public void processTelemetry(ProcessSessionTelemetryCommand command) {
        // 1. Находим текущую активную сессию
        ChargingSession session = sessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new IllegalArgumentException("Charging session not found with ID: " + command.sessionId()));

        // 2. Передаем новые данные счетчика и текущий тариф в доменную модель.
        // Домен сам рассчитает дельту кВт*ч, стоимость энергии и общую сумму инвойса totalFinalAmount.
        session.updateTelemetry(command.currentMeterValue(), command.currentKwhPrice());

        // 3. Сохраняем изменения. Поле version инкрементируется, защищая от Race Conditions.
        sessionRepository.save(session);
    }
}
