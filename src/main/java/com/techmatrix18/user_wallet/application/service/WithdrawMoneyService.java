package com.techmatrix18.user_wallet.application.service;

import com.techmatrix18.user_wallet.application.command.WithdrawMoneyCommand;
import com.techmatrix18.user_wallet.application.port.in.WithdrawMoneyUseCase;
import com.techmatrix18.user_wallet.application.port.out.BillingLedgerAuditRepository;
import com.techmatrix18.user_wallet.application.port.out.UserWalletRepository;
import com.techmatrix18.user_wallet.domain.UserWallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * WithdrawMoneyService
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 26.08.2026
 */

@Service
public class WithdrawMoneyService implements WithdrawMoneyUseCase {

    private final UserWalletRepository walletRepository;
    private final BillingLedgerAuditRepository auditRepository;
    // Здесь также может внедряться выходной порт внешней платежной системы (например, PaymentGatewayPort)

    public WithdrawMoneyService(UserWalletRepository walletRepository, BillingLedgerAuditRepository auditRepository) {
        this.walletRepository = walletRepository;
        this.auditRepository = auditRepository;
    }

    @Override
    @Transactional
    public void withdrawMoney(WithdrawMoneyCommand command) {
        UserWallet wallet = walletRepository.findByUserId(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user: " + command.userId()));

        // Сущность выбросит исключение, если баланса не хватает
        wallet.withdraw(command.amount());
        walletRepository.save(wallet);

        // Интеграция с банком (вызов внешнего шлюза) происходит здесь через порт
        // paymentGatewayPort.transferToCard(command.bankAccountReference(), command.amount());

        auditRepository.logOperation(
                command.userId(),
                0L, // Нет инвойса зарядки, ставим заглушку
                "USER_WITHDRAWAL",
                command.amount().negate(),
                wallet.getBalance(),
                "Payout to bank reference: " + command.bankAccountReference()
        );
    }
}

