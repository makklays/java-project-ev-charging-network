package com.techmatrix18.ledger_audit_log.insrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * JpaBillingLedgerRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

@Repository
public interface JpaBillingLedgerRepository extends JpaRepository<LedgerAuditLogEntity, Long> {

    // Порционная выборка устаревших логов для выгрузки в холодное хранилище (S3/ClickHouse)
    @Query(
            value = "SELECT * FROM billing_ledger_audit_log WHERE created_at < :cutOffDate ORDER BY id ASC LIMIT :limit",
            nativeQuery = true
    )
    List<LedgerAuditLogEntity> findOldEntries(
            @Param("cutOffDate") ZonedDateTime cutOffDate,
            @Param("limit") int limit
    );

    // Высокопроизводительное пакетное удаление заархивированных строк из операционной таблицы PostgreSQL
    @Modifying
    @Query(value = "DELETE FROM ledger_audit_logs WHERE id IN :ids", nativeQuery = true)
    void deleteEntriesByIds(@Param("ids") List<Long> ids);

    void deleteByIdIn(List<Long> ids);
}

