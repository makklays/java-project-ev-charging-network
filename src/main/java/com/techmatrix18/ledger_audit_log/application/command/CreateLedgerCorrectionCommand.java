package com.techmatrix18.ledger_audit_log.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * CreateLedgerCorrectionCommand
 * Команда администратора или финансового отдела для внесения корректирующей проводки (сторнирования)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 29.08.2026
 */

public record CreateLedgerCorrectionCommand(
        Long userId,
        Long chargingInvoiceId,
        BigDecimal amount,                 // Сумма исправления (может быть как +50.0000 для возврата, так и -50.0000)
        BigDecimal walletBalanceSnapshot,  // Слепок баланса кошелька ПОСЛЕ применения этой корректировки
        String auditComment                // Обязательное обоснование ручной правки (например, "Refund due to connector #1 hardware freeze")
) {
    public CreateLedgerCorrectionCommand {
        Objects.requireNonNull(userId, "User ID is required for ledger correction");
        Objects.requireNonNull(chargingInvoiceId, "Charging invoice ID is required for reference");
        Objects.requireNonNull(amount, "Correction amount cannot be null");
        Objects.requireNonNull(walletBalanceSnapshot, "Wallet balance snapshot cannot be null");
        Objects.requireNonNull(auditComment, "Audit comment with reason is mandatory for manual corrections");

        // Бизнес-валидация ручного вмешательства
        if (auditComment.isBlank()) {
            throw new IllegalArgumentException("You must provide a valid reason comment for this financial correction");
        }
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Correction amount cannot be zero");
        }
    }
}

