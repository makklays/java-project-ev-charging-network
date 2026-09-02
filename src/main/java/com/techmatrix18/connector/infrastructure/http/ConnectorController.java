package com.techmatrix18.connector.infrastructure.http;

import com.techmatrix18.building_blocks.infrastructure.interceptors.RequireIdempotency;
import com.techmatrix18.connector.application.command.*;
import com.techmatrix18.connector.application.port.in.*;
import com.techmatrix18.connector.application.port.out.ConnectorRepository;
import com.techmatrix18.connector.domain.Connector;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ConnectorController
 * HTTP-адаптер REST API для управления коннекторами (кабелями) сети EV Charging network
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@RestController
@RequestMapping("/api/v1/connectors")
public class ConnectorController {

    private final ConnectorRepository connectorRepository;
    private final RegisterConnectorUseCase registerConnectorUseCase;
    private final UpdateConnectorSpecsUseCase updateConnectorSpecsUseCase;
    private final OccupyConnectorUseCase occupyConnectorUseCase;
    private final FreeConnectorUseCase freeConnectorUseCase;
    private final ReportConnectorFaultUseCase reportConnectorFaultUseCase;

    // Внедрение зависимостей через конструктор
    public ConnectorController(
            ConnectorRepository connectorRepository,
            RegisterConnectorUseCase registerConnectorUseCase,
            UpdateConnectorSpecsUseCase updateConnectorSpecsUseCase,
            OccupyConnectorUseCase occupyConnectorUseCase,
            FreeConnectorUseCase freeConnectorUseCase,
            ReportConnectorFaultUseCase reportConnectorFaultUseCase
    ) {
        this.connectorRepository = connectorRepository;
        this.registerConnectorUseCase = registerConnectorUseCase;
        this.updateConnectorSpecsUseCase = updateConnectorSpecsUseCase;
        this.occupyConnectorUseCase = occupyConnectorUseCase;
        this.freeConnectorUseCase = freeConnectorUseCase;
        this.reportConnectorFaultUseCase = reportConnectorFaultUseCase;
    }

    // Получение текущего состояния и метаданных конкретного кабеля по ID
    @GetMapping("/{id}")
    public ResponseEntity<Connector> getConnectorById(@PathVariable Long id) {
        Connector connector = connectorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Connector not found with ID: " + id));
        return ResponseEntity.ok(connector);
    }

    // Монтаж и регистрация нового зарядного кабеля на точке EVSE
    @PostMapping
    @RequireIdempotency
    public ResponseEntity<Void> registerConnector(@Valid @RequestBody RegisterConnectorCommand command) {
        registerConnectorUseCase.registerConnector(command);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // HTTP 201 Created
    }

    // Техническая модернизация характеристик пистолета (замена типа разъема или мощности)
    @PutMapping("/specs")
    public ResponseEntity<Void> updateSpecs(@Valid @RequestBody UpdateConnectorSpecsCommand command) {
        updateConnectorSpecsUseCase.updateConnectorSpecs(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Сигнал IoT: Начало протекания тока через кабель (перевод в статус CHARGING)
    @PostMapping("/occupy")
    @RequireIdempotency
    public ResponseEntity<Void> occupyConnector(@Valid @RequestBody OccupyConnectorCommand command) {
        occupyConnectorUseCase.occupyConnector(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Сигнал IoT: Возврат пистолета в ложемент станции, кабель свободен (перевод в статус AVAILABLE)
    @PostMapping("/free")
    @RequireIdempotency
    public ResponseEntity<Void> freeConnector(@Valid @RequestBody FreeConnectorCommand command) {
        freeConnectorUseCase.freeConnector(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Сигнал IoT: Критический перегрев или поломка замка пистолета (перевод в статус FAULTED)
    @PostMapping("/report-fault")
    public ResponseEntity<Void> reportFault(@Valid @RequestBody ReportConnectorFaultCommand command) {
        reportConnectorFaultUseCase.reportConnectorFault(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}

