package com.techmatrix18.connector.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JpaConnectorRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Repository
public interface JpaConnectorRepository extends JpaRepository<ConnectorEntity, Long> {

    // Проверка уникальности комбинации evse_id и connector_number (из ограничения UNIQUE)
    boolean existsByEvseIdAndConnectorNumber(Long evseId, Integer connectorNumber);
}

