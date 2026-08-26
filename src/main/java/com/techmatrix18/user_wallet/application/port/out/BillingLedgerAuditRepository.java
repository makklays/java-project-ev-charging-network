package com.techmatrix18.user_wallet.application.port.out;

import java.math.BigDecimal;

/**
 * BillingLedgerAuditRepository
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 26.08.2026
 */

public interface BillingLedgerAuditRepository {
    void logOperation(Long userId, Long referenceId, String operationType, BigDecimal amount, BigDecimal currentBalance, String comment);
}

