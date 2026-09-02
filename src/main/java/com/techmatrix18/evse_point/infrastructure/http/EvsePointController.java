package com.techmatrix18.evse_point.infrastructure.http;

import com.techmatrix18.building_blocks.infrastructure.interceptors.RequireIdempotency;
import com.techmatrix18.evse_point.application.command.*;
import com.techmatrix18.evse_point.application.port.in.*;
import com.techmatrix18.evse_point.application.port.out.EvsePointRepository;
import com.techmatrix18.evse_point.domain.EvsePoint;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * EvsePointController
 * HTTP-адаптер REST API для управления точками зарядки (EVSE) сети EV Charging network
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@RestController
@RequestMapping("/api/v1/evse-points")
public class EvsePointController {

    private final EvsePointRepository evseRepository; // Используется строго для быстрого чтения данных (Queries)
    private final CreateEvsePointUseCase createEvsePointUseCase;
    private final PlugInCableUseCase plugInCableUseCase;
    private final StartEvseChargingUseCase startEvseChargingUseCase;
    private final SuspendEvseByVehicleUseCase suspendEvseByVehicleUseCase;
    private final ReportEvseHardwareFaultUseCase reportEvseHardwareFaultUseCase;
    private final ReleaseEvsePointUseCase releaseEvsePointUseCase;

    // Внедрение зависимостей через конструктор
    public EvsePointController(
            EvsePointRepository evseRepository,
            CreateEvsePointUseCase createEvsePointUseCase,
            PlugInCableUseCase plugInCableUseCase,
            StartEvseChargingUseCase startEvseChargingUseCase,
            SuspendEvseByVehicleUseCase suspendEvseByVehicleUseCase,
            ReportEvseHardwareFaultUseCase reportEvseHardwareFaultUseCase,
            ReleaseEvsePointUseCase releaseEvsePointUseCase
    ) {
        this.evseRepository = evseRepository;
        this.createEvsePointUseCase = createEvsePointUseCase;
        this.plugInCableUseCase = plugInCableUseCase;
        this.startEvseChargingUseCase = startEvseChargingUseCase;
        this.suspendEvseByVehicleUseCase = suspendEvseByVehicleUseCase;
        this.reportEvseHardwareFaultUseCase = reportEvseHardwareFaultUseCase;
        this.releaseEvsePointUseCase = releaseEvsePointUseCase;
    }

    // Получение текущего состояния и метаданных точки зарядки по ID
    @GetMapping("/{id}")
    public ResponseEntity<EvsePoint> getEvsePointById(@PathVariable Long id) {
        EvsePoint evsePoint = evseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("EVSE point not found with ID: " + id));
        return ResponseEntity.ok(evsePoint);
    }

    // Монтаж и инициализация нового зарядного порта (EVSE) на станции
    @PostMapping
    @RequireIdempotency
    public ResponseEntity<Void> createEvsePoint(@Valid @RequestBody CreateEvsePointCommand command) {
        createEvsePointUseCase.createEvsePoint(command);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // HTTP 201 Created
    }

    // Сигнал IoT: Физическое подключение кабеля к машине (перевод в статус PREPARING)
    @PostMapping("/plug-in")
    public ResponseEntity<Void> plugInCable(@Valid @RequestBody PlugInCableCommand command) {
        plugInCableUseCase.plugInCable(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Сигнал IoT: Успешный старт транзакции и запуск подачи тока (перевод в статус CHARGING)
    @PostMapping("/start-charging")
    @RequireIdempotency
    public ResponseEntity<Void> startCharging(@Valid @RequestBody StartEvseChargingCommand command) {
        startEvseChargingUseCase.startCharging(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Сигнал IoT: Временная приостановка закупки энергии со стороны машины (перевод в статус SUSPENDED_EV)
    @PostMapping("/suspend")
    public ResponseEntity<Void> suspendCharging(@Valid @RequestBody SuspendEvseByVehicleCommand command) {
        suspendEvseByVehicleUseCase.suspendCharging(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Сигнал IoT: Критический аппаратный сбой контроллера порта (перевод в статус FAULTED)
    @PostMapping("/report-fault")
    public ResponseEntity<Void> reportFault(@Valid @RequestBody ReportEvseHardwareFaultCommand command) {
        reportEvseHardwareFaultUseCase.reportFault(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Сигнал IoT: Отключение кабеля водителем, завершение сессии (перевод в статус AVAILABLE)
    @PostMapping("/release")
    @RequireIdempotency
    public ResponseEntity<Void> releaseEvsePoint(@Valid @RequestBody ReleaseEvsePointCommand command) {
        releaseEvsePointUseCase.releaseEvsePoint(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}

