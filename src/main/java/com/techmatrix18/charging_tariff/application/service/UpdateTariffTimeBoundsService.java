package com.techmatrix18.charging_tariff.application.service;

import com.techmatrix18.charging_tariff.application.command.UpdateTariffTimeBoundsCommand;
import com.techmatrix18.charging_tariff.application.port.in.UpdateTariffTimeBoundsUseCase;
import com.techmatrix18.charging_tariff.application.port.out.ChargingTariffRepository;
import com.techmatrix18.charging_tariff.domain.ChargingTariff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpdateTariffTimeBoundsService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class UpdateTariffTimeBoundsService implements UpdateTariffTimeBoundsUseCase {

    private final ChargingTariffRepository tariffRepository;

    public UpdateTariffTimeBoundsService(ChargingTariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    @Transactional
    public void updateTimeBounds(UpdateTariffTimeBoundsCommand command) {
        ChargingTariff tariff = tariffRepository.findById(command.tariffId())
                .orElseThrow(() -> new IllegalArgumentException("Tariff not found with ID: " + command.tariffId()));

        // Мутирующий метод доменного слоя Rich Model
        tariff.updateTimeBounds(command.startTime(), command.endTime());

        tariffRepository.save(tariff);
    }
}

