package com.techmatrix18.charging_tariff.application.service;

import com.techmatrix18.charging_tariff.application.command.UpdateTariffPricingCommand;
import com.techmatrix18.charging_tariff.application.port.in.UpdateTariffPricingUseCase;
import com.techmatrix18.charging_tariff.application.port.out.ChargingTariffRepository;
import com.techmatrix18.charging_tariff.domain.ChargingTariff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpdateTariffPricingService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class UpdateTariffPricingService implements UpdateTariffPricingUseCase {

    private final ChargingTariffRepository tariffRepository;

    public UpdateTariffPricingService(ChargingTariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    @Transactional
    public void updatePricing(UpdateTariffPricingCommand command) {
        ChargingTariff tariff = tariffRepository.findById(command.tariffId())
                .orElseThrow(() -> new IllegalArgumentException("Tariff not found with ID: " + command.tariffId()));

        // Мутирующий метод доменного слоя Rich Model
        tariff.updatePricing(command.pricePerKwh(), command.idlePricePerMin());

        tariffRepository.save(tariff);
    }
}

