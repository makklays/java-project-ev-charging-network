package com.techmatrix18.user_wallet.application.service;

import com.techmatrix18.user_wallet.application.command.SettleFinalInvoiceCommand;
import com.techmatrix18.user_wallet.application.port.in.SettleFinalInvoiceUseCase;
import com.techmatrix18.user_wallet.application.port.out.BillingLedgerAuditRepository;
import com.techmatrix18.user_wallet.application.port.out.UserWalletRepository;
import com.techmatrix18.user_wallet.domain.UserWallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SettleFinalInvoiceService
 * Реализация бизнес-логики финального расчета по инвойсу зарядной сессии
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class SettleFinalInvoiceService implements SettleFinalInvoiceUseCase {

    private final UserWalletRepository walletRepository;
    private final BillingLedgerAuditRepository auditRepository;

    public SettleFinalInvoiceService(UserWalletRepository walletRepository, BillingLedgerAuditRepository auditRepository) {
        this.walletRepository = walletRepository;
        this.auditRepository = auditRepository;
    }

    @Override
    @Transactional // Гарантирует атомарность: списание и запись в лог пройдут в одной транзакции
    public void settleFinalInvoice(SettleFinalInvoiceCommand command) {
        // Извлекаем кошелек пользователя (включается Optimistic Locking через поле version)
        UserWallet wallet = walletRepository.findByUserId(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user ID: " + command.userId()));

        // Списываем финальную сумму инвойса (включая НДС) внутри доменной сущности кошелька
        wallet.withdraw(command.finalAmount());

        // Сохраняем обновленный баланс кошелька в базу данных
        walletRepository.save(wallet);

        // Фиксируем окончательную проводку в append-only аудит-логе (Миграция №12)
        // Сумма передается со знаком минус (дебетование счета)
        String auditComment = String.format("Final invoice settlement. Invoice ID: %d", command.invoiceId());

        auditRepository.logOperation(
                command.userId(),
                command.invoiceId(),
                "INVOICE_FINAL_SETTLEMENT", // Тип финансовой операции из миграции
                command.finalAmount().negate(), // Отрицательное значение для списания
                wallet.getBalance(), // wallet_balance_snapshot после списания
                auditComment
        );
    }
}

