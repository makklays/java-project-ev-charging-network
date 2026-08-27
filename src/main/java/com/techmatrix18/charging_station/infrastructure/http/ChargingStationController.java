package com.techmatrix18.charging_station.infrastructure.http;

import com.techmatrix18.charging_station.application.command.*;
import com.techmatrix18.charging_station.application.port.in.*;
import com.techmatrix18.charging_station.application.port.out.ChargingStationRepository;
import com.techmatrix18.charging_station.domain.ChargingStation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ChargingStationController
 * HTTP-адаптер REST API для управления зарядными станциями (локациями) сети EV Charging network
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@RestController
@RequestMapping("/api/v1/stations")
public class ChargingStationController {

    private final ChargingStationRepository stationRepository;
    private final RegisterStationUseCase registerStationUseCase;
    private final UpdateStationDetailsUseCase updateStationDetailsUseCase;
    private final UpdateStationGeoUseCase updateStationGeoUseCase;
    private final SetStationOnlineUseCase setStationOnlineUseCase;
    private final SetStationOfflineUseCase setStationOfflineUseCase;
    private final PutStationOnMaintenanceUseCase putStationOnMaintenanceUseCase;
    private final RebalanceStationPowerUseCase rebalanceStationPowerUseCase;

    // Внедряем зависимости через конструктор
    public ChargingStationController(
            ChargingStationRepository stationRepository,
            RegisterStationUseCase registerStationUseCase,
            UpdateStationDetailsUseCase updateStationDetailsUseCase,
            UpdateStationGeoUseCase updateStationGeoUseCase,
            SetStationOnlineUseCase setStationOnlineUseCase,
            SetStationOfflineUseCase setStationOfflineUseCase,
            PutStationOnMaintenanceUseCase putStationOnMaintenanceUseCase,
            RebalanceStationPowerUseCase rebalanceStationPowerUseCase
    ) {
        this.stationRepository = stationRepository;
        this.registerStationUseCase = registerStationUseCase;
        this.updateStationDetailsUseCase = updateStationDetailsUseCase;
        this.updateStationGeoUseCase = updateStationGeoUseCase;
        this.setStationOnlineUseCase = setStationOnlineUseCase;
        this.setStationOfflineUseCase = setStationOfflineUseCase;
        this.putStationOnMaintenanceUseCase = putStationOnMaintenanceUseCase;
        this.rebalanceStationPowerUseCase = rebalanceStationPowerUseCase;
    }

    // Получение метаданных конкретной зарядной станции по ID
    @GetMapping("/{id}")
    public ResponseEntity<ChargingStation> getStationById(@PathVariable Long id) {
        ChargingStation station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Charging station not found with ID: " + id));
        return ResponseEntity.ok(station);
    }

    // Регистрация новой зарядной локации в сети
    @PostMapping
    public ResponseEntity<Void> registerStation(@Valid @RequestBody RegisterStationCommand command) {
        registerStationUseCase.registerStation(command);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // HTTP 201 Created
    }

    // Изменение текстовых метаданных (имя, адрес)
    @PutMapping("/details")
    public ResponseEntity<Void> updateDetails(@Valid @RequestBody UpdateStationDetailsCommand command) {
        updateStationDetailsUseCase.updateStationDetails(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Точная корректировка гео-координат на карте
    @PutMapping("/geo")
    public ResponseEntity<Void> updateGeo(@Valid @RequestBody UpdateStationGeoCommand command) {
        updateStationGeoUseCase.updateStationGeo(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Принудительный перевод станции в рабочий режим (Online)
    @PostMapping("/{id}/online")
    public ResponseEntity<Void> setOnline(@PathVariable Long id) {
        setStationOnlineUseCase.setStationOnline(new SetStationOnlineCommand(id));
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Аварийное отключение станции (вызывается IoT-воркером при потере Heartbeat)
    @PostMapping("/{id}/offline")
    public ResponseEntity<Void> setOffline(@PathVariable Long id) {
        setStationOfflineUseCase.setStationOffline(new SetStationOfflineCommand(id));
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Блокировка станции для проведения технического обслуживания/ремонта
    @PostMapping("/{id}/maintenance")
    public ResponseEntity<Void> putOnMaintenance(@PathVariable Long id) {
        putStationOnMaintenanceUseCase.putStationOnMaintenance(new PutStationOnMaintenanceCommand(id));
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Динамическая балансировка общей доступной мощности хаба
    @PutMapping("/rebalance")
    public ResponseEntity<Void> rebalancePower(@Valid @RequestBody RebalanceStationPowerCommand command) {
        rebalanceStationPowerUseCase.rebalanceStationPower(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}

