package com.techmatrix18.connector.infrastructure.db;

import com.techmatrix18.connector.domain.Connector;
import com.techmatrix18.connector.domain.ConnectorStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * ConnectorEntity
 * JPA Сущность для таблицы "connectors" (Слой инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Entity
@Table(
        name = "connectors",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_evse_connector_number", columnNames = {"evse_id", "connector_number"})
        }
)
public class ConnectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evse_id", nullable = false)
    private Long evseId;

    @Column(name = "connector_number", nullable = false)
    private Integer connectorNumber;

    @Column(name = "connector_type", nullable = false, length = 50)
    private String connectorType;

    @Column(name = "current_type", nullable = false, length = 10)
    private String currentType;

    @Column(name = "max_power_kw", nullable = false, precision = 6, scale = 2)
    private BigDecimal maxPowerKw;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ConnectorStatus status;

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
    public ConnectorEntity() {
    }

    // --- Маппинг: Из чистого Домена в JPA Entity (Для сохранения в БД) ---
    public static ConnectorEntity fromDomain(Connector domain) {
        if (domain == null) return null;

        ConnectorEntity entity = new ConnectorEntity();
        entity.id = domain.getId();
        entity.evseId = domain.getEvseId();
        entity.connectorNumber = domain.getConnectorNumber();
        entity.connectorType = domain.getConnectorType();
        entity.currentType = domain.getCurrentType();
        entity.maxPowerKw = domain.getMaxPowerKw();
        entity.status = domain.getStatus();
        entity.version = domain.getVersion(); // Передаем текущую версию для контроля блокировок
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }

    // --- Маппинг: Из JPA Entity в чистый Домен (Для чтения из БД) ---
    public Connector toDomain() {
        return new Connector(
                this.id,
                this.evseId,
                this.connectorNumber,
                this.connectorType,
                this.currentType,
                this.maxPowerKw,
                this.status,
                this.version, // Возвращаем версию в домен для контроля параллельных запросов
                this.createdAt,
                this.updatedAt
        );
    }

    // --- Стандартные геттеры и сеттеры для JPA ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEvseId() { return evseId; }
    public void setEvseId(Long evseId) { this.evseId = evseId; }

    public Integer getConnectorNumber() { return connectorNumber; }
    public void setConnectorNumber(Integer connectorNumber) { this.connectorNumber = connectorNumber; }

    public String getConnectorType() { return connectorType; }
    public void setConnectorType(String connectorType) { this.connectorType = connectorType; }

    public String getCurrentType() { return currentType; }
    public void setCurrentType(String currentType) { this.currentType = currentType; }

    public BigDecimal getMaxPowerKw() { return maxPowerKw; }
    public void setMaxPowerKw(BigDecimal maxPowerKw) { this.maxPowerKw = maxPowerKw; }

    public ConnectorStatus getStatus() { return status; }
    public void setStatus(ConnectorStatus status) { this.status = status; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}

