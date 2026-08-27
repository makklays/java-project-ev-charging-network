package com.techmatrix18.evse_point.infrastructure.db;

import com.techmatrix18.evse_point.domain.EvsePoint;
import com.techmatrix18.evse_point.domain.EvseStatus;
import jakarta.persistence.*;
import java.time.ZonedDateTime;

/**
 * EvsePointEntity
 * JPA Сущность для таблицы "evse_points" (Слой инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Entity
@Table(
        name = "evse_points",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_station_evse_number", columnNames = {"station_id", "evse_number"})
        }
)
public class EvsePointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_id", nullable = false)
    private Long stationId;

    @Column(name = "evse_number", nullable = false)
    private Integer evseNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EvseStatus status;

    @Column(name = "ocpp_evse_id", nullable = false)
    private Integer ocppEvseId;

    @Version // Активирует Optimistic Locking на основе поля version BIGINT из вашей миграции
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // --- Автоматическое управление временными метками жизненного цикла JPA ---
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = ZonedDateTime.now();
        }
        this.updatedAt = ZonedDateTime.now();
    }

    @PreUpdate // Метод автоматически вызывается Hibernate перед каждым UPDATE запросом в базу
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    // --- Конструкторы ---
    public EvsePointEntity() {
    }

    // --- Маппинг: Из чистого Домена в JPA Entity (Для сохранения в БД) ---
    public static EvsePointEntity fromDomain(EvsePoint domain) {
        if (domain == null) return null;

        EvsePointEntity entity = new EvsePointEntity();
        entity.id = domain.getId();
        entity.stationId = domain.getStationId();
        entity.evseNumber = domain.getEvseNumber();
        entity.status = domain.getStatus();
        entity.ocppEvseId = domain.getOcppEvseId();
        entity.version = domain.getVersion(); // Передаем текущую версию для контроля блокировок
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }

    // --- Маппинг: Из JPA Entity в чистый Домен (Для чтения из БД) ---
    public EvsePoint toDomain() {
        return new EvsePoint(
                this.id,
                this.stationId,
                this.evseNumber,
                this.status,
                this.ocppEvseId,
                this.version, // Возвращаем версию в домен для контроля параллельных запросов
                this.createdAt,
                this.updatedAt
        );
    }

    // --- Стандартные геттеры и сеттеры для JPA ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }

    public Integer getEvseNumber() { return evseNumber; }
    public void setEvseNumber(Integer evseNumber) { this.evseNumber = evseNumber; }

    public EvseStatus getStatus() { return status; }
    public void setStatus(EvseStatus status) { this.status = status; }

    public Integer getOcppEvseId() { return ocppEvseId; }
    public void setOcppEvseId(Integer ocppEvseId) { this.ocppEvseId = ocppEvseId; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}

