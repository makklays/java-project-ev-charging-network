package com.techmatrix18.building_blocks.infrastructure.db;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * OutboxEventEntity
 * JPA Сущность для таблицы "outbox_events" (Слой технической инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 01.09.2026
 */

@Entity
@Table(name = "outbox_events")
public class OutboxEventEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "aggregate_id", nullable = false, columnDefinition = "UUID")
    private UUID aggregateId;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "event_type", nullable = false, length = 255)
    private String eventType;

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OutboxStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "processed_at")
    private ZonedDateTime processedAt;

    // --- Автоматическое управление временными метками жизненного цикла JPA ---

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = ZonedDateTime.now();
        }
        if (this.status == null) {
            this.status = OutboxStatus.PENDING;
        }
    }

    // --- Конструкторы ---

    public OutboxEventEntity() {
    }

    // --- Геттеры и Сеттеры ---

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getAggregateId() { return aggregateId; }
    public void setAggregateId(UUID aggregateId) { this.aggregateId = aggregateId; }

    public String getAggregateType() { return aggregateType; }
    public void setAggregateType(String aggregateType) { this.aggregateType = aggregateType; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    public OutboxStatus getStatus() { return status; }
    public void setStatus(OutboxStatus status) { this.status = status; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(ZonedDateTime processedAt) { this.processedAt = processedAt; }

    // Статусы обработки события фоновым реле-процессором (Message Relay / Scheduler)
    public enum OutboxStatus {
        PENDING,    // Ожидает отправки в брокер (Kafka/RabbitMQ)
        PROCESSED,  // Успешно отправлено
        FAILED      // Ошибка отправки (исчерпаны попытки retry)
    }
}

