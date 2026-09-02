package com.techmatrix18.building_blocks.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.ZonedDateTime;

/**
 * JpaIdempotencyRecordRepository
 * Репозиторий Spring Data JPA для управления записями идемпотентности (Слой технической инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 01.09.2026
 */

@Repository
public interface JpaIdempotencyRecordRepository extends JpaRepository<IdempotencyRecordEntity, String> {

    /**
     * Удаляет все записи, у которых истек срок действия (expires_at меньше текущего времени).
     * Метод будет вызываться фоновым планировщиком (Cron/Scheduler) для очистки старых логов.
     *
     * @param now Текущее время системы (ZonedDateTime)
     * @return Количество удаленных записей
     */
    @Modifying
    @Query("DELETE FROM IdempotencyRecordEntity e WHERE e.expiresAt < :now")
    int deleteExpiredRecords(@Param("now") ZonedDateTime now);
}

