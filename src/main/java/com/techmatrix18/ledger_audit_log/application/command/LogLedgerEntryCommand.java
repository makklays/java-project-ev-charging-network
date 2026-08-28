package com.techmatrix18.ledger_audit_log.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * LogLedgerEntryCommand
 * Команда для создания новой неизменяемой записи в аудиторском финансовом журнале
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

public record LogLedgerEntryCommand(
        Long userId,
        Long chargingInvoiceId,
        String operationType,
        BigDecimal amount,
        BigDecimal walletBalanceSnapshot,
        BigDecimal historicalPricePerKwh,
        String historicalTariffName,
        BigDecimal deltaKwh,
        Integer deltaMinutes,
        BigDecimal totalMeterKwh,
        String auditComment
) {
    public LogLedgerEntryCommand {
        Objects.requireNonNull(userId, "User ID is required for ledger audit logging");
        Objects.requireNonNull(chargingInvoiceId, "Charging invoice ID is required for ledger audit logging");
        Objects.requireNonNull(operationType, "Operation type descriptor cannot be null");
        Objects.requireNonNull(amount, "Financial entry amount cannot be null");
        Objects.requireNonNull(walletBalanceSnapshot, "Wallet balance snapshot cannot be null");
        Objects.requireNonNull(deltaKwh, "Delta kWh consumed volume cannot be null");
        Objects.requireNonNull(deltaMinutes, "Delta minutes duration count cannot be null");
        Objects.requireNonNull(totalMeterKwh, "Total absolute meter value cannot be null");

        // Финтех и логические валидации параметров проводки
        if (operationType.isBlank()) {
            throw new IllegalArgumentException("Financial operation type cannot be empty");
        }
        if (deltaKwh.compareTo(BigDecimal.ZERO) < 0 || totalMeterKwh.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Physical meter metric deltas cannot hold negative values");
        }
        if (deltaMinutes < 0) {
            throw new IllegalArgumentException("Time delta cannot hold negative duration values");
        }
    }
}

