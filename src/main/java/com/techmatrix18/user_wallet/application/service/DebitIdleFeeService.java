package com.techmatrix18.user_wallet.application.service;

import com.techmatrix18.user_wallet.application.command.DebitIdleFeeCommand;
import com.techmatrix18.user_wallet.application.port.in.DebitIdleFeeUseCase;
import com.techmatrix18.user_wallet.application.port.out.BillingLedgerAuditRepository;
import com.techmatrix18.user_wallet.application.port.out.UserWalletRepository;
import com.techmatrix18.user_wallet.domain.UserWallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DebitIdleFeeService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 26.08.2026
 */

@Service
public class DebitIdleFeeService implements DebitIdleFeeUseCase {

    private final UserWalletRepository walletRepository;
    private final BillingLedgerAuditRepository auditRepository;

    public DebitIdleFeeService(UserWalletRepository walletRepository, BillingLedgerAuditRepository auditRepository) {
        this.walletRepository = walletRepository;
        this.auditRepository = auditRepository;
    }

    @Override
    @Transactional
    public void debitIdleFee(DebitIdleFeeCommand command) {
        UserWallet wallet = walletRepository.findByUserId(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user: " + command.userId()));

        // Вызов доменного метода списания денег
        wallet.withdraw(command.amount());
        walletRepository.save(wallet);

        // Запись в append-only аудит-лог биллинга (Миграция №12)
        auditRepository.logOperation(
                command.userId(),
                command.sessionId(),
                "IDLE_FEE_DEBIT",
                command.amount().negate(), // Записываем как минус в проводку
                wallet.getBalance(),       // wallet_balance_snapshot
                "Idle fee charge for session " + command.sessionId()
        );
    }
}

