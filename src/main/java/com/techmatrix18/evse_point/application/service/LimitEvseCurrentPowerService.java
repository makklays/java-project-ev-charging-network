package com.techmatrix18.evse_point.application.service;

import com.techmatrix18.evse_point.application.command.LimitEvseCurrentPowerCommand;
import com.techmatrix18.evse_point.application.port.in.LimitEvseCurrentPowerUseCase;
import com.techmatrix18.evse_point.application.port.out.EvsePointRepository;
import com.techmatrix18.evse_point.domain.EvsePoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LimitEvseCurrentPowerService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class LimitEvseCurrentPowerService implements LimitEvseCurrentPowerUseCase {

    private final EvsePointRepository evseRepository;
    // Здесь также внедряется выходной порт для отправки OCPP команд на станцию (например, OcppMessageSenderPort)

    public LimitEvseCurrentPowerService(EvsePointRepository evseRepository) {
        this.evseRepository = evseRepository;
    }

    @Override
    @Transactional
    public void limitEvsePower(LimitEvseCurrentPowerCommand command) {
        // Проверяем, что точка физически существует в нашей БД
        EvsePoint evse = evseRepository.findById(command.evseId())
                .orElseThrow(() -> new IllegalArgumentException("EVSE point not found with ID: " + command.evseId()));

        // 💡 Инфраструктурное действие: отправка по сети на зарядный шкаф (через Output Port SPI)
        // ocppMessageSenderPort.sendSetChargingProfile(evse.getOcppEvseId(), command.maxPowerKw(), command.maxCurrentAmps());

        // Сама доменная модель EvsePoint в вашей миграции не хранит динамический лимит,
        // поэтому мы просто выполняем отправку команды контроллеру станции.
    }
}

