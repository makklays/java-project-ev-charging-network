package com.techmatrix18.charging_station.application.service;

import com.techmatrix18.charging_station.application.command.UpdateStationGeoCommand;
import com.techmatrix18.charging_station.application.port.in.UpdateStationGeoUseCase;
import com.techmatrix18.charging_station.application.port.out.ChargingStationRepository;
import com.techmatrix18.charging_station.domain.ChargingStation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpdateStationGeoService
 * Реализация бизнес-логики обновления гео-координат локации зарядок
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class UpdateStationGeoService implements UpdateStationGeoUseCase {

    private final ChargingStationRepository stationRepository;

    public UpdateStationGeoService(ChargingStationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional // Обеспечивает атомарность операции и контроль версии записи СУБД
    public void updateStationGeo(UpdateStationGeoCommand command) {
        // Извлекаем доменную модель станции через выходной порт
        ChargingStation station = stationRepository.findById(command.stationId())
                .orElseThrow(() -> new IllegalArgumentException("Charging station not found with ID: " + command.stationId()));

        // Вызываем доменный бизнес-метод изменения координат Rich Model (код метода приведен ниже)
        station.updateGeo(command.latitude(), command.longitude());

        // Сохраняем измененное состояние в базу данных
        stationRepository.save(station);
    }
}

