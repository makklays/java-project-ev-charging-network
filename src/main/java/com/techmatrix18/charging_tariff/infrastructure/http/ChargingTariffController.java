package com.techmatrix18.charging_tariff.infrastructure.http;

import com.techmatrix18.building_blocks.infrastructure.interceptors.RequireIdempotency;
import com.techmatrix18.charging_tariff.application.command.CreateTariffCommand;
import com.techmatrix18.charging_tariff.application.command.DeleteTariffCommand;
import com.techmatrix18.charging_tariff.application.command.UpdateTariffPricingCommand;
import com.techmatrix18.charging_tariff.application.command.UpdateTariffTimeBoundsCommand;
import com.techmatrix18.charging_tariff.application.port.in.CreateTariffUseCase;
import com.techmatrix18.charging_tariff.application.port.in.DeleteTariffUseCase;
import com.techmatrix18.charging_tariff.application.port.in.UpdateTariffPricingUseCase;
import com.techmatrix18.charging_tariff.application.port.in.UpdateTariffTimeBoundsUseCase;
import com.techmatrix18.charging_tariff.application.port.out.ChargingTariffRepository;
import com.techmatrix18.charging_tariff.domain.ChargingTariff;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ChargingTariffController
 * HTTP-адаптер REST API для управления тарифами биллинга сети EV Charging network
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@RestController
@RequestMapping("/api/v1/tariffs")
public class ChargingTariffController {

    private final ChargingTariffRepository tariffRepository; // Используется строго для быстрого чтения данных (Queries)
    private final CreateTariffUseCase createTariffUseCase;
    private final UpdateTariffPricingUseCase updateTariffPricingUseCase;
    private final UpdateTariffTimeBoundsUseCase updateTariffTimeBoundsUseCase;
    private final DeleteTariffUseCase deleteTariffUseCase;

    // Внедрение зависимостей через конструктор
    public ChargingTariffController(
            ChargingTariffRepository tariffRepository,
            CreateTariffUseCase createTariffUseCase,
            UpdateTariffPricingUseCase updateTariffPricingUseCase,
            UpdateTariffTimeBoundsUseCase updateTariffTimeBoundsUseCase,
            DeleteTariffUseCase deleteTariffUseCase
    ) {
        this.tariffRepository = tariffRepository;
        this.createTariffUseCase = createTariffUseCase;
        this.updateTariffPricingUseCase = updateTariffPricingUseCase;
        this.updateTariffTimeBoundsUseCase = updateTariffTimeBoundsUseCase;
        this.deleteTariffUseCase = deleteTariffUseCase;
    }

    // Получение метаданных конкретного тарифа по ID
    @GetMapping("/{id}")
    public ResponseEntity<ChargingTariff> getTariffById(@PathVariable Long id) {
        ChargingTariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tariff not found with ID: " + id));
        return ResponseEntity.ok(tariff);
    }

    // Создание и заведение новой тарифной зоны (сетки) для коннектора
    @PostMapping
    @RequireIdempotency
    public ResponseEntity<Void> createTariff(@Valid @RequestBody CreateTariffCommand command) {
        createTariffUseCase.createTariff(command);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // HTTP 201 Created
    }

    // Корректировка цен за кВт*ч и времени простоя в существующем тарифе
    @PutMapping("/pricing")
    public ResponseEntity<Void> updatePricing(@Valid @RequestBody UpdateTariffPricingCommand command) {
        updateTariffPricingUseCase.updatePricing(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Изменение (сдвиг) суточных временных рамок действия тарифа
    @PutMapping("/time-bounds")
    public ResponseEntity<Void> updateTimeBounds(@Valid @RequestBody UpdateTariffTimeBoundsCommand command) {
        updateTariffTimeBoundsUseCase.updateTimeBounds(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Удаление тарифной зоны из биллинговой системы
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTariff(@PathVariable Long id) {
        deleteTariffUseCase.deleteTariff(new DeleteTariffCommand(id));
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}

