package com.techmatrix18.evse_point.application.service;

import com.techmatrix18.evse_point.application.command.StartEvseChargingCommand;
import com.techmatrix18.evse_point.application.port.in.StartEvseChargingUseCase;
import com.techmatrix18.evse_point.application.port.out.EvsePointRepository;
import com.techmatrix18.evse_point.domain.EvsePoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * StartEvseChargingService
 * Реализация бизнес-логики запуска процесса зарядки на точке (перевод в статус CHARGING)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class StartEvseChargingService implements StartEvseChargingUseCase {

    private final EvsePointRepository evseRepository;

    public StartEvseChargingService(EvsePointRepository evseRepository) {
        this.evseRepository = evseRepository;
    }

    @Override
    @Transactional // Атомарно изолирует транзакцию изменения статуса и активирует Optimistic Locking
    public void startCharging(StartEvseChargingCommand command) {
        // Извлекаем доменную модель зарядной точки (EVSE) через выходной порт
        EvsePoint evse = evseRepository.findById(command.evseId())
                .orElseThrow(() -> new IllegalArgumentException("EVSE point not found with ID: " + command.evseId()));

        // Вызываем доменный бизнес-метод Rich Model для перевода в состояние CHARGING
        evse.startCharging();

        // Фиксируем изменения в репозитории постоянного хранения
        evseRepository.save(evse);

        // 💡 Связь контекстов через события (Event-Driven Architecture):
        // Сюда можно добавить публикацию события в Kafka (например, EvseChargingStartedEvent),
        // на которое среагирует модуль финансового биллинга, чтобы запустить таймер
        // периодических списаний денег с кошелька пользователя по сессии command.sessionId().
    }
}

