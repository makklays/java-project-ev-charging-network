package com.techmatrix18.charging_invoice.application.service;

import com.techmatrix18.charging_invoice.application.command.MarkInvoiceAsFailedCommand;
import com.techmatrix18.charging_invoice.application.port.in.MarkInvoiceAsFailedUseCase;
import com.techmatrix18.charging_invoice.application.port.out.ChargingInvoiceRepository;
import com.techmatrix18.charging_invoice.domain.ChargingInvoice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MarkInvoiceAsFailedService
 * Переводит счет в статус FAILED, если на карте клиента нет денег или его кошелек пуст.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class MarkInvoiceAsFailedService implements MarkInvoiceAsFailedUseCase {

    private final ChargingInvoiceRepository invoiceRepository;

    public MarkInvoiceAsFailedService(ChargingInvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    @Transactional
    public void markAsFailed(MarkInvoiceAsFailedCommand command) {
        ChargingInvoice invoice = invoiceRepository.findById(command.invoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + command.invoiceId()));

        // Переводим доменную модель в статус FAILED. Запрещает порчу уже оплаченных архивных чеков.
        invoice.markAsFailed();

        // Сохраняем неплатежеспособный статус в базу данных
        invoiceRepository.save(invoice);

        //  Здесь можно инициировать отправку push-нотификации пользователю: "Пополните баланс для разблокировки зарядок"
    }
}

