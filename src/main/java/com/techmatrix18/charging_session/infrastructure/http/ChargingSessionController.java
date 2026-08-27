package com.techmatrix18.charging_session.infrastructure.http;

import com.techmatrix18.charging_session.application.command.*;
import com.techmatrix18.charging_session.application.port.in.*;
import com.techmatrix18.charging_session.application.port.out.ChargingSessionRepository;
import com.techmatrix18.charging_session.domain.ChargingSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ChargingSessionController
 * HTTP-адаптер REST API для управления зарядными сессиями и биллингом в сети EV Charging network
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@RestController
@RequestMapping("/api/v1/charging-sessions")
public class ChargingSessionController {

    private final ChargingSessionRepository sessionRepository; // Используется строго для быстрого чтения данных (Queries)
    private final StartChargingSessionUseCase startChargingSessionUseCase;
    private final ProcessSessionTelemetryUseCase processSessionTelemetryUseCase;
    private final ApplySessionIdleFeeUseCase applySessionIdleFeeUseCase;
    private final CompleteChargingSessionUseCase completeChargingSessionUseCase;
    private final FailChargingSessionUseCase failChargingSessionUseCase;

    // Внедрение зависимостей через конструктор
    public ChargingSessionController(
            ChargingSessionRepository sessionRepository,
            StartChargingSessionUseCase startChargingSessionUseCase,
            ProcessSessionTelemetryUseCase processSessionTelemetryUseCase,
            ApplySessionIdleFeeUseCase applySessionIdleFeeUseCase,
            CompleteChargingSessionUseCase completeChargingSessionUseCase,
            FailChargingSessionUseCase failChargingSessionUseCase
    ) {
        this.sessionRepository = sessionRepository;
        this.startChargingSessionUseCase = startChargingSessionUseCase;
        this.processSessionTelemetryUseCase = processSessionTelemetryUseCase;
        this.applySessionIdleFeeUseCase = applySessionIdleFeeUseCase;
        this.completeChargingSessionUseCase = completeChargingSessionUseCase;
        this.failChargingSessionUseCase = failChargingSessionUseCase;
    }

    // Получение текущих финансовых и физических метрик сессии по ID
    @GetMapping("/{id}")
    public ResponseEntity<ChargingSession> getSessionById(@PathVariable Long id) {
        ChargingSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Charging session not found with ID: " + id));
        return ResponseEntity.ok(session);
    }

    // Инициализация и запуск новой зарядной сессии (вызывается водителем из приложения)
    @PostMapping
    public ResponseEntity<Void> startSession(@Valid @RequestBody StartChargingSessionCommand command) {
        startChargingSessionUseCase.startSession(command);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // HTTP 201 Created
    }

    // Сигнал IoT: Потоковое обновление счетчика кВт*ч со станции (OCPP MeterValues)
    @PutMapping("/telemetry")
    public ResponseEntity<Void> processTelemetry(@Valid @RequestBody ProcessSessionTelemetryCommand command) {
        processSessionTelemetryUseCase.processTelemetry(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Сигнал Системы/Крона: Начисление штрафа за простой на парковочном месте
    @PutMapping("/idle-fee")
    public ResponseEntity<Void> applyIdleFee(@Valid @RequestBody ApplySessionIdleFeeCommand command) {
        applySessionIdleFeeUseCase.applyIdleFee(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Штатное завершение зарядной сессии (отключение кабеля или кнопка Стоп в приложении)
    @PostMapping("/complete")
    public ResponseEntity<Void> completeSession(@Valid @RequestBody CompleteChargingSessionCommand command) {
        completeChargingSessionUseCase.completeSession(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Аварийное прерывание сессии при критических сбоях оборудования или IoT-связи
    @PostMapping("/fail")
    public ResponseEntity<Void> failSession(@Valid @RequestBody FailChargingSessionCommand command) {
        failChargingSessionUseCase.failSession(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}

