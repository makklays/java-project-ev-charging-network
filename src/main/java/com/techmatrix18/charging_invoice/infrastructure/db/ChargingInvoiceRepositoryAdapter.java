package com.techmatrix18.charging_invoice.infrastructure.db;

import com.techmatrix18.charging_invoice.application.port.out.ChargingInvoiceRepository;
import com.techmatrix18.charging_invoice.domain.ChargingInvoice;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * ChargingInvoiceRepositoryAdapter
 * Выходной адаптер для управления постоянным хранением бухгалтерских счетов в JPA
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Component
public class ChargingInvoiceRepositoryAdapter implements ChargingInvoiceRepository {

    private final JpaChargingInvoiceRepository repository;

    // Внедряем Spring Data репозиторий через конструктор
    public ChargingInvoiceRepositoryAdapter(JpaChargingInvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public ChargingInvoice save(ChargingInvoice invoice) {
        // Конвертируем чистый Домен в JPA Entity
        ChargingInvoiceEntity entity = ChargingInvoiceEntity.fromDomain(invoice);

        // Сохраняем в базу данных через Spring Data
        ChargingInvoiceEntity savedEntity = repository.save(entity);

        // Возвращаем обратно чистую доменную модель с обновленным ID и version
        return savedEntity.toDomain();
    }

    @Override
    public Optional<ChargingInvoice> findById(Long id) {
        // Извлекаем Entity из БД и, если она найдена, маппим в Домен
        return repository.findById(id).map(ChargingInvoiceEntity::toDomain);
    }
}

