package com.techmatrix18.building_blocks.infrastructure.schedule;

import com.techmatrix18.building_blocks.infrastructure.db.JpaIdempotencyRecordRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;

/**
 * IdempotencyCleanUpScheduler
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 01.09.2026
 */

@Component
public class IdempotencyCleanUpScheduler {

    private final JpaIdempotencyRecordRepository repository;

    public IdempotencyCleanUpScheduler(JpaIdempotencyRecordRepository repository) {
        this.repository = repository;
    }

    // Запуск раз в час для удаления ключей, созданных более 24 часов назад
    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void cleanExpiredKeys() {
        repository.deleteExpiredRecords(ZonedDateTime.now());
    }
}

