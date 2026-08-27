package com.techmatrix18.evse_point.application.service;

import com.techmatrix18.evse_point.application.command.SuspendEvseByVehicleCommand;
import com.techmatrix18.evse_point.application.port.in.SuspendEvseByVehicleUseCase;
import com.techmatrix18.evse_point.application.port.out.EvsePointRepository;
import com.techmatrix18.evse_point.domain.EvsePoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SuspendEvseByVehicleService
 * Реализация бизнес-логики приостановки зарядки (перевод в статус SUSPENDED_EV)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class SuspendEvseByVehicleService implements SuspendEvseByVehicleUseCase {

    private final EvsePointRepository evseRepository;

    public SuspendEvseByVehicleService(EvsePointRepository evseRepository) {
        this.evseRepository = evseRepository;
    }

    @Override
    @Transactional // Атомарно изолирует транзакцию изменения статуса и активирует Optimistic Locking
    public void suspendCharging(SuspendEvseByVehicleCommand command) {
        // Извлекаем доменную модель зарядной точки (EVSE) через выходной порт
        EvsePoint evse = evseRepository.findById(command.evseId())
                .orElseThrow(() -> new IllegalArgumentException("EVSE point not found with ID: " + command.evseId()));

        // Вызываем доменный бизнес-метод Rich Model для перевода в состояние SUSPENDED_EV
        evse.suspendByEv();

        // Фиксируем изменения в репозитории постоянного хранения
        evseRepository.save(evse);

        //  Интеграция с биллингом:
        // Здесь можно опубликовать доменное событие EvseChargingSuspendedEvent в Kafka.
        // Финансовый модуль перехватит его по sessionId и временно остановит тарификацию
        // чистых киловатт-часов, при необходимости запустив счетчик ожидания.
    }
}

