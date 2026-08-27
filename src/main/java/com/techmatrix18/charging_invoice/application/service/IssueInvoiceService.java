package com.techmatrix18.charging_invoice.application.service;

import com.techmatrix18.charging_invoice.application.command.IssueInvoiceCommand;
import com.techmatrix18.charging_invoice.application.port.in.IssueInvoiceUseCase;
import com.techmatrix18.charging_invoice.application.port.out.ChargingInvoiceRepository;
import com.techmatrix18.charging_invoice.domain.ChargingInvoice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * IssueInvoiceService
 * Вызывается биллинг-движком сразу после успешного завершения зарядной сессии.
 * Генерирует юридически значимый документ с автоматическим расчетом НДС 20%.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class IssueInvoiceService implements IssueInvoiceUseCase {

    private final ChargingInvoiceRepository invoiceRepository;

    public IssueInvoiceService(ChargingInvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    @Transactional // Открывает транзакцию СУБД для атомарной вставки записи инвойса
    public void issueInvoice(IssueInvoiceCommand command) {
        // 1. Инициализируем Rich Domain Model через бизнес-конструктор.
        // Он автоматически рассчитает НДС (VAT 20%) и финальную сумму к списанию finalAmountWithVat.
        ChargingInvoice newInvoice = new ChargingInvoice(
                command.sessionId(),
                command.userId(),
                command.invoiceNumber(),
                command.chargingDurationMinutes(),
                command.idleMinutes(),
                command.consumedKwh(),
                command.energyAmount(),
                command.idleAmount()
        );

        // 2. Сохраняем инвойс в СУБД. Ограничение UNIQUE защитит от двойного выставления счета по одной сессии.
        invoiceRepository.save(newInvoice);
    }
}

