package com.techmatrix18.user_wallet.application.service;

import com.techmatrix18.user_wallet.application.command.DepositMoneyCommand;
import com.techmatrix18.user_wallet.application.port.in.DepositMoneyUseCase;
import com.techmatrix18.user_wallet.application.port.out.BillingLedgerAuditRepository;
import com.techmatrix18.user_wallet.application.port.out.UserWalletRepository;
import com.techmatrix18.user_wallet.domain.UserWallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DepositMoneyService
 * Реализация бизнес-логики пополнения кошелька пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class DepositMoneyService implements DepositMoneyUseCase {

    private final UserWalletRepository walletRepository;
    private final BillingLedgerAuditRepository auditRepository;

    public DepositMoneyService(UserWalletRepository walletRepository, BillingLedgerAuditRepository auditRepository) {
        this.walletRepository = walletRepository;
        this.auditRepository = auditRepository;
    }

    @Override
    @Transactional
    public void depositMoney(DepositMoneyCommand command) {
        // 1. Извлекаем кошелек пользователя по его ID
        UserWallet wallet = walletRepository.findByUserId(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user ID: " + command.userId()));

        // 2. Вызываем доменный метод пополнения баланса внутри сущности (Rich Domain Model)
        wallet.deposit(command.amount());

        // 3. Сохраняем обновленный кошелек в базу данных
        walletRepository.save(wallet);

        // 4. Фиксируем операцию в append-only аудит-логе (Миграция №12)
        // Сумма передается как положительное число (кредитная проводка)
        String auditComment = String.format("Successful payment. Ref: %s", command.paymentReference());

        auditRepository.logOperation(
                command.userId(),
                0L, // Операция пополнения не связана с зарядным инвойсом, передаем технический 0
                "USER_DEPOSIT", // Тип проводки для бухгалтерии
                command.amount(), // Плюс к балансу
                wallet.getBalance(), // Фиксируем баланс ПОСЛЕ зачисления средств
                auditComment
        );
    }
}

