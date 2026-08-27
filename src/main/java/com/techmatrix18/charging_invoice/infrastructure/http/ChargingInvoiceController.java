package com.techmatrix18.charging_invoice.infrastructure.http;

import com.techmatrix18.charging_invoice.application.command.DisputeInvoiceCommand;
import com.techmatrix18.charging_invoice.application.command.IssueInvoiceCommand;
import com.techmatrix18.charging_invoice.application.command.MarkInvoiceAsFailedCommand;
import com.techmatrix18.charging_invoice.application.command.PayInvoiceCommand;
import com.techmatrix18.charging_invoice.application.port.in.DisputeInvoiceUseCase;
import com.techmatrix18.charging_invoice.application.port.in.IssueInvoiceUseCase;
import com.techmatrix18.charging_invoice.application.port.in.MarkInvoiceAsFailedUseCase;
import com.techmatrix18.charging_invoice.application.port.in.PayInvoiceUseCase;
import com.techmatrix18.charging_invoice.application.port.out.ChargingInvoiceRepository;
import com.techmatrix18.charging_invoice.domain.ChargingInvoice;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ChargingInvoiceController
 * HTTP-адаптер REST API для управления бухгалтерскими счетами и инвойсами сети EV Charging network
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@RestController
@RequestMapping("/api/v1/charging-invoices")
public class ChargingInvoiceController {

    private final ChargingInvoiceRepository invoiceRepository; // Используется строго для быстрого чтения данных (Queries)
    private final IssueInvoiceUseCase issueInvoiceUseCase;
    private final PayInvoiceUseCase payInvoiceUseCase;
    private final MarkInvoiceAsFailedUseCase markInvoiceAsFailedUseCase;
    private final DisputeInvoiceUseCase disputeInvoiceUseCase;

    // Внедрение зависимостей через конструктор
    public ChargingInvoiceController(
            ChargingInvoiceRepository invoiceRepository,
            IssueInvoiceUseCase issueInvoiceUseCase,
            PayInvoiceUseCase payInvoiceUseCase,
            MarkInvoiceAsFailedUseCase markInvoiceAsFailedUseCase,
            DisputeInvoiceUseCase disputeInvoiceUseCase
    ) {
        this.invoiceRepository = invoiceRepository;
        this.issueInvoiceUseCase = issueInvoiceUseCase;
        this.payInvoiceUseCase = payInvoiceUseCase;
        this.markInvoiceAsFailedUseCase = markInvoiceAsFailedUseCase;
        this.disputeInvoiceUseCase = disputeInvoiceUseCase;
    }

    // Получение метаданных конкретного счета по ID
    @GetMapping("/{id}")
    public ResponseEntity<ChargingInvoice> getInvoiceById(@PathVariable Long id) {
        ChargingInvoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + id));
        return ResponseEntity.ok(invoice);
    }

    // Системный триггер: Генерация и выставление нового счета с НДС (вызывается биллинг-движком)
    @PostMapping
    public ResponseEntity<Void> issueInvoice(@Valid @RequestBody IssueInvoiceCommand command) {
        issueInvoiceUseCase.issueInvoice(command);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // HTTP 201 Created
    }

    // Вебхук эквайринга: Фиксация успешного списания денег (перевод в статус PAID)
    @PostMapping("/pay")
    public ResponseEntity<Void> payInvoice(@Valid @RequestBody PayInvoiceCommand command) {
        payInvoiceUseCase.payInvoice(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Вебхук эквайринга: Сбой транзакции / недостаточный баланс кошелька (перевод в статус FAILED)
    @PostMapping("/fail")
    public ResponseEntity<Void> markAsFailed(@Valid @RequestBody MarkInvoiceAsFailedCommand command) {
        markInvoiceAsFailedUseCase.markAsFailed(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Административный триггер: Заморозка счета на время разбора жалобы в поддержке (перевод в статус DISPUTED)
    @PostMapping("/dispute")
    public ResponseEntity<Void> disputeInvoice(@Valid @RequestBody DisputeInvoiceCommand command) {
        disputeInvoiceUseCase.disputeInvoice(command);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}

