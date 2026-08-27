package com.techmatrix18.charging_station.application.service;

import com.techmatrix18.charging_station.application.command.UpdateStationDetailsCommand;
import com.techmatrix18.charging_station.application.port.in.UpdateStationDetailsUseCase;
import com.techmatrix18.charging_station.application.port.out.ChargingStationRepository;
import com.techmatrix18.charging_station.domain.ChargingStation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpdateStationDetailsService
 * Реализация бизнес-логики обновления публичных данных станции
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class UpdateStationDetailsService implements UpdateStationDetailsUseCase {

    private final ChargingStationRepository stationRepository;

    public UpdateStationDetailsService(ChargingStationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional // Активирует фиксацию транзакции и паттерн Optimistic Locking при сохранении
    public void updateStationDetails(UpdateStationDetailsCommand command) {
        // Извлекаем доменную модель станции из базы данных через выходной порт
        ChargingStation station = stationRepository.findById(command.stationId())
                .orElseThrow(() -> new IllegalArgumentException("Charging station not found with ID: " + command.stationId()));

        // Делегируем изменение полей внутренней бизнес-логике сущности
        // Метод сущности изменит поля и обновит updatedAt текущим временем
        station.updateDetails(command.name(), command.address());

        // Сохраняем обновленный объект в репозиторий
        stationRepository.save(station);
    }
}

