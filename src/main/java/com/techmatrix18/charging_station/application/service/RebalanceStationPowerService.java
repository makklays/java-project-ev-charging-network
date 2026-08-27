package com.techmatrix18.charging_station.application.service;

import com.techmatrix18.charging_station.application.command.RebalanceStationPowerCommand;
import com.techmatrix18.charging_station.application.port.in.RebalanceStationPowerUseCase;
import com.techmatrix18.charging_station.application.port.out.ChargingStationRepository;
import com.techmatrix18.charging_station.domain.ChargingStation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RebalanceStationPowerService
 * Реализация бизнес-логики динамической балансировки мощности локации
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class RebalanceStationPowerService implements RebalanceStationPowerUseCase {

    private final ChargingStationRepository stationRepository;

    public RebalanceStationPowerService(ChargingStationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional
    public void rebalanceStationPower(RebalanceStationPowerCommand command) {
        // Находим доменную сущность станции через выходной порт
        ChargingStation station = stationRepository.findById(command.stationId())
                .orElseThrow(() -> new IllegalArgumentException("Charging station not found with ID: " + command.stationId()));

        // Вызываем доменный бизнес-метод изменения мощности Rich Model (его код приведен ниже)
        station.rebalancePower(command.newMaxPowerKw());

        // Сохраняем измененное состояние в постоянное хранилище
        stationRepository.save(station);
    }
}

