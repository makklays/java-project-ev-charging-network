package com.techmatrix18.charging_tariff.application.service;

import com.techmatrix18.charging_tariff.application.command.DeleteTariffCommand;
import com.techmatrix18.charging_tariff.application.port.in.DeleteTariffUseCase;
import com.techmatrix18.charging_tariff.application.port.out.ChargingTariffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DeleteTariffService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class DeleteTariffService implements DeleteTariffUseCase {

    private final ChargingTariffRepository tariffRepository;
    // Здесь также может внедряться порт для проверки использования тарифа в активных сессиях зарядок
    // private final ChargingSessionValidationPort sessionValidation;

    public DeleteTariffService(ChargingTariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    @Transactional
    public void deleteTariff(DeleteTariffCommand command) {
        // Проверяем, существует ли тариф перед удалением
        tariffRepository.findById(command.tariffId())
                .orElseThrow(() -> new IllegalArgumentException("Tariff not found with ID: " + command.tariffId()));

        //  Дополнительное бизнес-правило безопасности:
        // if (sessionValidationPort.hasActiveSessionsWithTariff(command.tariffId())) {
        //     throw new IllegalStateException("Cannot delete tariff because it is actively used in an ongoing charging session");
        // }

        tariffRepository.deleteById(command.tariffId());
    }
}

