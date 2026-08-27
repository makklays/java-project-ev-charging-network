package com.techmatrix18.charging_invoice.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * IssueInvoiceCommand
 * Автоматическая генерация и выставление счета с расчетом НДС 20%
 * Команда для формирования и фиксации нового бухгалтерского счета за зарядку
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record IssueInvoiceCommand(
        Long sessionId,
        Long userId,
        String invoiceNumber,
        Integer chargingDurationMinutes,
        Integer idleMinutes,
        BigDecimal consumedKwh,
        BigDecimal energyAmount,
        BigDecimal idleAmount
) {
    public IssueInvoiceCommand {
        Objects.requireNonNull(sessionId, "Session ID is required");
        Objects.requireNonNull(userId, "User ID is required");
        Objects.requireNonNull(invoiceNumber, "Invoice number is required");
        Objects.requireNonNull(chargingDurationMinutes, "Charging duration minutes count is required");
        Objects.requireNonNull(idleMinutes, "Idle minutes count is required");
        Objects.requireNonNull(consumedKwh, "Consumed kWh capacity volume cannot be null");
        Objects.requireNonNull(energyAmount, "Energy cost amount cannot be null");
        Objects.requireNonNull(idleAmount, "Idle cost amount cannot be null");

        // Финтех-валидация параметров выставляемого документа
        if (invoiceNumber.isBlank()) {
            throw new IllegalArgumentException("Invoice number cannot be empty or blank string");
        }
        if (chargingDurationMinutes < 0 || idleMinutes < 0) {
            throw new IllegalArgumentException("Time intervals cannot hold negative durations");
        }
        if (consumedKwh.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Electricity consumption volume cannot be negative");
        }
        if (energyAmount.compareTo(BigDecimal.ZERO) < 0 || idleAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invoice financial positions cannot hold negative values");
        }
    }
}

