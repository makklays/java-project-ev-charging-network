package com.techmatrix18.evse_point.application.service;

import com.techmatrix18.evse_point.application.command.CreateEvsePointCommand;
import com.techmatrix18.evse_point.application.port.in.CreateEvsePointUseCase;
import com.techmatrix18.evse_point.application.port.out.EvsePointRepository;
import com.techmatrix18.evse_point.domain.EvsePoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CreateEvsePointService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class CreateEvsePointService implements CreateEvsePointUseCase {

    private final EvsePointRepository evseRepository;

    public CreateEvsePointService(EvsePointRepository evseRepository) {
        this.evseRepository = evseRepository;
    }

    @Override
    @Transactional
    public void createEvsePoint(CreateEvsePointCommand command) {
        // Проверка уникальности номера EVSE в рамках конкретной станции (Бизнес-инвариант)
        if (evseRepository.existsByStationIdAndEvseNumber(command.stationId(), command.evseNumber())) {
            throw new IllegalStateException(String.format(
                    "EVSE point with number %d already exists on station ID: %d",
                    command.evseNumber(), command.stationId()));
        }

        // Инициализация Rich Domain Model (выставит статус AVAILABLE)
        EvsePoint newEvse = new EvsePoint(
                command.stationId(),
                command.evseNumber(),
                command.ocppEvseId()
        );

        evseRepository.save(newEvse);
    }
}

