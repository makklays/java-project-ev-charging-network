package com.techmatrix18.charging_station.application.service;

import com.techmatrix18.charging_station.application.command.SetStationOnlineCommand;
import com.techmatrix18.charging_station.application.port.in.SetStationOnlineUseCase;
import com.techmatrix18.charging_station.application.port.out.ChargingStationRepository;
import com.techmatrix18.charging_station.domain.ChargingStation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SetStationOnlineService
 * Реализация бизнес-логики перевода зарядной станции в статус Online
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class SetStationOnlineService implements SetStationOnlineUseCase {

    private final ChargingStationRepository stationRepository;

    public SetStationOnlineService(ChargingStationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional // Гарантирует атомарность и запускает проверку Optimistic Locking по полю version
    public void setStationOnline(SetStationOnlineCommand command) {
        // Извлекаем доменную модель станции из базы данных через выходной порт
        ChargingStation station = stationRepository.findById(command.stationId())
                .orElseThrow(() -> new IllegalArgumentException("Charging station not found with ID: " + command.stationId()));

        // Вызываем доменный бизнес-метод изменения статуса на ONLINE
        // Метод автоматически обновит поле updatedAt текущим временем
        station.setOnline();

        // Сохраняем обновленную сущность обратно в базу данных
        stationRepository.save(station);
    }
}

