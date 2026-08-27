package com.techmatrix18.charging_tariff.application.service;

import com.techmatrix18.charging_tariff.application.command.CreateTariffCommand;
import com.techmatrix18.charging_tariff.application.port.in.CreateTariffUseCase;
import com.techmatrix18.charging_tariff.application.port.out.ChargingTariffRepository;
import com.techmatrix18.charging_tariff.domain.ChargingTariff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CreateTariffService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class CreateTariffService implements CreateTariffUseCase {

    private final ChargingTariffRepository tariffRepository;

    public CreateTariffService(ChargingTariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    @Transactional
    public void createTariff(CreateTariffCommand command) {
        // Создание новой доменной сущности через конструктор бизнес-валидации
        ChargingTariff tariff = new ChargingTariff(
                command.connectorId(),
                command.zoneName(),
                command.startTime(),
                command.endTime(),
                command.pricePerKwh(),
                command.idlePricePerMin()
        );

        tariffRepository.save(tariff);
    }
}

