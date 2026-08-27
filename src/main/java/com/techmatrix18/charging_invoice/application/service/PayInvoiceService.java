package com.techmatrix18.charging_invoice.application.service;

import com.techmatrix18.charging_invoice.application.command.PayInvoiceCommand;
import com.techmatrix18.charging_invoice.application.port.in.PayInvoiceUseCase;
import com.techmatrix18.charging_invoice.application.port.out.ChargingInvoiceRepository;
import com.techmatrix18.charging_invoice.domain.ChargingInvoice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PayInvoiceService
 * Вызывается финтех-модулем кошельков (UserWallet) или Stripe вебхуком после успешного списания денег.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class PayInvoiceService implements PayInvoiceUseCase {

    private final ChargingInvoiceRepository invoiceRepository;

    public PayInvoiceService(ChargingInvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    @Transactional // Запускает Optimistic Locking проверку версии перед закрытием счета
    public void payInvoice(PayInvoiceCommand command) {
        // Находим выставленный инвойс в базе данных
        ChargingInvoice invoice = invoiceRepository.findById(command.invoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + command.invoiceId()));

        // Делегируем изменение состояния доменной Rich Model.
        // Переведет статус в PAID и зафиксирует точное время оплаты в paidAt.
        invoice.markAsPaid();

        // Сохраняем обновленный закрытый инвойс в постоянное хранилище
        invoiceRepository.save(invoice);
    }
}

