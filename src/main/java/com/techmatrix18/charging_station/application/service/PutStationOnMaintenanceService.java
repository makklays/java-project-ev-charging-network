package com.techmatrix18.charging_station.application.service;

import com.techmatrix18.charging_station.application.command.PutStationOnMaintenanceCommand;
import com.techmatrix18.charging_station.application.port.in.PutStationOnMaintenanceUseCase;
import com.techmatrix18.charging_station.application.port.out.ChargingStationRepository;
import com.techmatrix18.charging_station.domain.ChargingStation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PutStationOnMaintenanceService
 * Реализация бизнес-логики перевода станции в режим обслуживания
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class PutStationOnMaintenanceService implements PutStationOnMaintenanceUseCase {

    private final ChargingStationRepository stationRepository;

    public PutStationOnMaintenanceService(ChargingStationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional
    public void putStationOnMaintenance(PutStationOnMaintenanceCommand command) {
        // Извлекаем доменную модель зарядной станции из репозитория (выходного порта)
        ChargingStation station = stationRepository.findById(command.stationId())
                .orElseThrow(() -> new IllegalArgumentException("Charging station not found with ID: " + command.stationId()));

        // Вызываем доменный бизнес-метод Rich Model (он проверит, не находится ли станция уже в ремонте)
        // и переведет статус в ChargingStationStatus.UNDER_MAINTENANCE
        station.startMaintenance();

        // Сохраняем обновленное состояние станции в базу данных
        stationRepository.save(station);

        // Архитектурная рекомендация для продакшена:
        // Здесь также можно опубликовать доменное событие StationMaintenanceStartedEvent в Kafka,
        // чтобы мобильные приложения водителей мгновенно узнали, что станция недоступна для зарядки.
    }
}

