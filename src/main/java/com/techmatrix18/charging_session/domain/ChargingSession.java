package com.techmatrix18.charging_session.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ChargingSession
 * Чистая доменная сущность зарядной сессии (Бизнес-ядро IoT и Биллинга)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public class ChargingSession {

    private final Long id;
    private final Long userId;
    private final Long connectorId;
    private ChargingSessionStatus status;
    private final ZonedDateTime startedAt;
    private ZonedDateTime finishedAt;
    private final BigDecimal startMeterValue;
    private BigDecimal lastMeterValue;
    private BigDecimal totalKwhConsumed;
    private BigDecimal totalEnergyAmount;
    private BigDecimal totalIdleAmount;
    private BigDecimal totalFinalAmount;
    private final Long version;
    private final ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    // Список событий, произошедших с агрегатом в памяти
    private final List<Object> domainEvents = new ArrayList<>();

    /**
     * Конструктор для первичного создания (Старт зарядки пользователем через приложение)
     */
    public ChargingSession(Long userId, Long connectorId, BigDecimal startMeterValue) {
        this.id = null; // Будет сгенерирован СУБД (BIGSERIAL)
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.connectorId = Objects.requireNonNull(connectorId, "Connector ID cannot be null");
        this.startMeterValue = Objects.requireNonNull(startMeterValue, "Start meter value cannot be null");
        this.lastMeterValue = startMeterValue; // При старте значения счетчиков равны

        this.status = ChargingSessionStatus.IN_PROGRESS; // По умолчанию из миграции
        this.startedAt = ZonedDateTime.now();
        this.finishedAt = null;

        this.totalKwhConsumed = BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);
        this.totalEnergyAmount = BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        this.totalIdleAmount = BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        this.totalFinalAmount = BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);

        this.version = 0L;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();

        validatePhysicalLimits();
    }

    /**
     * Конструктор для восстановления сущности из базы данных (Маппинг инфраструктуры)
     */
    public ChargingSession(Long id, Long userId, Long connectorId, ChargingSessionStatus status,
                           ZonedDateTime startedAt, ZonedDateTime finishedAt, BigDecimal startMeterValue,
                           BigDecimal lastMeterValue, BigDecimal totalKwhConsumed, BigDecimal totalEnergyAmount,
                           BigDecimal totalIdleAmount, BigDecimal totalFinalAmount, Long version,
                           ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "Session ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.connectorId = Objects.requireNonNull(connectorId, "Connector ID cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.startedAt = Objects.requireNonNull(startedAt, "StartedAt date cannot be null");
        this.finishedAt = finishedAt;
        this.startMeterValue = Objects.requireNonNull(startMeterValue, "Start meter value cannot be null");
        this.lastMeterValue = Objects.requireNonNull(lastMeterValue, "Last meter value cannot be null");
        this.totalKwhConsumed = Objects.requireNonNull(totalKwhConsumed, "Total kWh consumed cannot be null");
        this.totalEnergyAmount = Objects.requireNonNull(totalEnergyAmount, "Total energy amount cannot be null");
        this.totalIdleAmount = Objects.requireNonNull(totalIdleAmount, "Total idle amount cannot be null");
        this.totalFinalAmount = Objects.requireNonNull(totalFinalAmount, "Total final amount cannot be null");
        this.version = Objects.requireNonNull(version, "Version cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt date cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "UpdatedAt date cannot be null");

        validatePhysicalLimits();
    }

    // --- Инкапсулированные бизнес-методы (Rich Domain Model) ---

    /**
     * Периодическое обновление показаний счетчика со станции (IoT Телеметрия)
     * Высчитывает дельту кВт*ч и обновляет финансовые метрики сессии
     */
    public void updateTelemetry(BigDecimal currentMeterValue, BigDecimal currentKwhPrice) {
        if (this.status != ChargingSessionStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot update telemetry for finished or failed session");
        }
        Objects.requireNonNull(currentMeterValue, "Current meter value cannot be null");
        Objects.requireNonNull(currentKwhPrice, "Current kWh price cannot be null");

        if (currentMeterValue.compareTo(this.lastMeterValue) < 0) {
            throw new IllegalArgumentException("New meter value cannot be lower than the previous one");
        }

        this.lastMeterValue = currentMeterValue;
        // Расчет общего объема потребленной энергии с начала сессии
        this.totalKwhConsumed = this.lastMeterValue.subtract(this.startMeterValue).setScale(3, RoundingMode.HALF_UP);

        // Расчет стоимости только за чистую энергию
        this.totalEnergyAmount = this.totalKwhConsumed.multiply(currentKwhPrice).setScale(4, RoundingMode.HALF_UP);

        // Перерасчет итоговой суммы инвойса
        recalculateFinalSum();
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Начисление пени/штрафа за простой на парковочном месте после окончания заряда
     */
    public void applyIdleFee(BigDecimal idleAmountDelta) {
        Objects.requireNonNull(idleAmountDelta, "Idle amount delta cannot be null");
        if (idleAmountDelta.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Idle fee delta cannot be negative");
        }

        this.totalIdleAmount = this.totalIdleAmount.add(idleAmountDelta).setScale(4, RoundingMode.HALF_UP);
        recalculateFinalSum();
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Успешное штатное завершение зарядной сессии
     */
    public void complete() {
        if (this.status == ChargingSessionStatus.COMPLETED) {
            throw new IllegalStateException("Session is already completed");
        }
        this.status = ChargingSessionStatus.COMPLETED;
        this.finishedAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Фиксация аварийного прерывания сессии (например, станция обесточена или выбит пистолет)
     */
    public void fail() {
        this.status = ChargingSessionStatus.FAILED;
        this.finishedAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    private void recalculateFinalSum() {
        this.totalFinalAmount = this.totalEnergyAmount.add(this.totalIdleAmount).setScale(4, RoundingMode.HALF_UP);
    }

    private void validatePhysicalLimits() {
        if (this.startMeterValue.compareTo(BigDecimal.ZERO) < 0 || this.lastMeterValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Meter values cannot be negative physical values");
        }
        if (this.totalKwhConsumed.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Consumed energy cannot be negative");
        }
    }

    /**
     * Возвращает неизменяемый список произошедших доменных событий.
     */
    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Очищает список событий после их успешного сохранения инфраструктурным адаптером.
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    // --- Геттеры для портов и мапперов ---

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getConnectorId() { return connectorId; }
    public ChargingSessionStatus getStatus() { return status; }
    public ZonedDateTime getStartedAt() { return startedAt; }
    public ZonedDateTime getFinishedAt() { return finishedAt; }
    public BigDecimal getStartMeterValue() { return startMeterValue; }
    public BigDecimal getLastMeterValue() { return lastMeterValue; }
    public BigDecimal getTotalKwhConsumed() { return totalKwhConsumed; }
    public BigDecimal getTotalEnergyAmount() { return totalEnergyAmount; }
    public BigDecimal getTotalIdleAmount() { return totalIdleAmount; }
    public BigDecimal getTotalFinalAmount() { return totalFinalAmount; }
    public Long getVersion() { return version; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
}
