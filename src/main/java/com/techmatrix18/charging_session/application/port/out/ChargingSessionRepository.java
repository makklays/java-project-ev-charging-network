package com.techmatrix18.charging_session.application.port.out;

import com.techmatrix18.charging_session.domain.ChargingSession;
import java.util.Optional;

/**
 * ChargingSessionRepositoryPort
 * Выходной порт (SPI) для изоляции доменного ядра от деталей работы СУБД (PostgreSQL/JPA)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface ChargingSessionRepository {
    ChargingSession save(ChargingSession session);
    Optional<ChargingSession> findById(Long id);
}

