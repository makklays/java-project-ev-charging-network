package com.techmatrix18.building_blocks.infrastructure.db;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

/**
 * IdempotencyEntity
 * JPA Сущность для таблицы "idempotency_records" (Слой технической инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 01.09.2026
 */

@Entity
@Table(name = "idempotency_records")
public class IdempotencyRecordEntity {

    @Id
    @Column(name = "idempotency_key", nullable = false, length = 255)
    private String idempotencyKey;

    @Column(name = "request_payload_hash", nullable = false, length = 64)
    private String requestPayloadHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IdempotencyStatus status;

    @Column(name = "response_code")
    private Integer responseCode;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private ZonedDateTime expiresAt;

    // --- Автоматическое управление временными метками жизненного цикла JPA ---

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = ZonedDateTime.now();
        }
        if (this.expiresAt == null) {
            this.expiresAt = this.createdAt.plusHours(24); // Автоматически выставляем TTL на сутки
        }
    }

    // --- Конструкторы ---

    public IdempotencyRecordEntity() {
    }

    /**
     * Вспомогательный статический метод для инициализации новой записи (Статус STARTED)
     */
    public static IdempotencyRecordEntity createNew(String idempotencyKey, String requestPayloadHash) {
        IdempotencyRecordEntity entity = new IdempotencyRecordEntity();
        entity.idempotencyKey = idempotencyKey;
        entity.requestPayloadHash = requestPayloadHash;
        entity.status = IdempotencyStatus.STARTED;
        return entity;
    }

    // --- Геттеры и Сеттеры ---

    public String getIdempotencyKey() {
        return idempotencyKey;
    }
    public void setRequestPayloadHash(String requestPayloadHash) {
        this.requestPayloadHash = requestPayloadHash;
    }

    public IdempotencyStatus getStatus() {
        return status;
    }
    public void setStatus(IdempotencyStatus status) {
        this.status = status;
    }

    public Integer getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    // Внутренний перечень статусов для строгого соответствия CHECK-констреинту в БД
    public enum IdempotencyStatus {
        STARTED,
        PROCESSING,
        COMPLETED,
        FAILED
    }
}

