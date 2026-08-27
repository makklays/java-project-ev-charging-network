package com.techmatrix18.charging_invoice.application.service;

import com.techmatrix18.charging_invoice.application.command.DisputeInvoiceCommand;
import com.techmatrix18.charging_invoice.application.port.in.DisputeInvoiceUseCase;
import com.techmatrix18.charging_invoice.application.port.out.ChargingInvoiceRepository;
import com.techmatrix18.charging_invoice.domain.ChargingInvoice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DisputeInvoiceService
 * Замораживает автоматические санкции, если водитель подал официальную претензию по сумме инвойса.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class DisputeInvoiceService implements DisputeInvoiceUseCase {

    private final ChargingInvoiceRepository invoiceRepository;

    public DisputeInvoiceService(ChargingInvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    @Transactional
    public void disputeInvoice(DisputeInvoiceCommand command) {
        ChargingInvoice invoice = invoiceRepository.findById(command.invoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + command.invoiceId()));

        // Меняем внутреннее состояние доменной Rich Model на DISPUTED
        invoice.dispute();

        // Записываем статус блокировки в репозиторий СУБД
        invoiceRepository.save(invoice);
    }
}

