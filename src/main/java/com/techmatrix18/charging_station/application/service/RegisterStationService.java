package com.techmatrix18.charging_station.application.service;

import com.techmatrix18.charging_station.application.command.RegisterStationCommand;
import com.techmatrix18.charging_station.application.port.in.RegisterStationUseCase;
import com.techmatrix18.charging_station.application.port.out.ChargingStationRepository;
import com.techmatrix18.charging_station.domain.ChargingStation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RegisterStationService
 * Реализация бизнес-логики регистрации новой зарядной локации
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class RegisterStationService implements RegisterStationUseCase {

    private final ChargingStationRepository stationRepository;

    public RegisterStationService(ChargingStationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional
    public void registerStation(RegisterStationCommand command) {
        // Создаем чистую доменную сущность станции
        // Используется конструктор бизнес-регистрации, который мы заложили в домене
        ChargingStation newStation = new ChargingStation(
                command.name(),
                command.address(),
                command.latitude(),
                command.longitude(),
                command.maxPowerKw()
        );

        // Сохраняем новую сущность в базу данных через выходной порт
        stationRepository.save(newStation);
    }
}

