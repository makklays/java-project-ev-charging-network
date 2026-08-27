package com.techmatrix18.charging_station.application.service;

import com.techmatrix18.charging_station.application.command.SetStationOfflineCommand;
import com.techmatrix18.charging_station.application.port.in.SetStationOfflineUseCase;
import com.techmatrix18.charging_station.application.port.out.ChargingStationRepository;
import com.techmatrix18.charging_station.domain.ChargingStation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SetStationOfflineService
 * Реализация бизнес-логики перевода станции в статус Offline
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class SetStationOfflineService implements SetStationOfflineUseCase {

    private final ChargingStationRepository stationRepository;

    public SetStationOfflineService(ChargingStationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional // Активирует транзакцию СУБД и механизм Optimistic Locking при сохранении
    public void setStationOffline(SetStationOfflineCommand command) {
        // Находим доменную сущность станции в репозитории через выходной порт
        ChargingStation station = stationRepository.findById(command.stationId())
                .orElseThrow(() -> new IllegalArgumentException("Charging station not found with ID: " + command.stationId()));

        // Делегируем изменение состояния доменному бизнес-методу Rich Model
        // Меняет статус на ChargingStationStatus.OFFLINE и фиксирует время updatedAt
        station.setOffline();

        // Сохраняем обновленный объект обратно в постоянное хранилище
        stationRepository.save(station);
    }
}

