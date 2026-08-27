package com.techmatrix18.connector.application.port.out;

import com.techmatrix18.connector.domain.Connector;
import java.util.Optional;

/**
 * ConnectorRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface ConnectorRepository {
    Connector save(Connector connector);
    Optional<Connector> findById(Long id);
    boolean existsByEvseIdAndConnectorNumber(Long evseId, Integer connectorNumber);
}

