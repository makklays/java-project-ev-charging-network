package com.techmatrix18.charging_invoice.application.port.out;

import com.techmatrix18.charging_invoice.domain.ChargingInvoice;
import java.util.Optional;

/**
 * ChargingInvoiceRepositoryPort
 * Выходной порт (SPI) для изоляции доменного ядра от деталей работы СУБД (PostgreSQL/JPA)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public interface ChargingInvoiceRepository {
    ChargingInvoice save(ChargingInvoice invoice);
    Optional<ChargingInvoice> findById(Long id);
}

