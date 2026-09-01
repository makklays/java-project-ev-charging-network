package com.techmatrix18.building_blocks.infrastructure.db;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * JpaOutboxEventRepository
 * Репозиторий Spring Data JPA для управления очередью Outbox-событий (Слой технической инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 01.09.2026
 */

@Repository
public interface JpaOutboxEventRepository extends JpaRepository<OutboxEventEntity, Long> {

    /**
     * Выбирает список неотправленных событий, отсортированных по времени создания (FIFO).
     * Используется фоновым воркером для пакетной отправки сообщений в брокер.
     *
     * @param status Статус события (обычно OutboxStatus.PENDING)
     * @param pageable Ограничение количества записей (например, PageRequest.of(0, 100))
     * @return Список событий для обработки
     */
    List<OutboxEventEntity> findByStatusOrderByCreatedAtAsc(OutboxEventEntity.OutboxStatus status, Pageable pageable);

    /**
     * Удаляет старые, успешно обработанные события, чтобы таблица в БД не разрасталась.
     * Рекомендуется вызывать раз в сутки через Scheduler.
     *
     * @param status Статус обработки (OutboxStatus.PROCESSED)
     * @param beforeTime Граница времени (удалить всё, что обработано раньше этой даты)
     * @return Количество удаленных архивных записей
     */
    @Modifying
    @Query("DELETE FROM OutboxEventEntity e WHERE e.status = :status AND e.processedAt < :beforeTime")
    int deleteArchivedEvents(@Param("status") OutboxEventEntity.OutboxStatus status, @Param("beforeTime") ZonedDateTime beforeTime);
}

