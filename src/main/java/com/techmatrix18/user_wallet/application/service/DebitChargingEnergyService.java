package com.techmatrix18.user_wallet.application.service;

import com.techmatrix18.user_wallet.application.command.DebitChargingEnergyCommand;
import com.techmatrix18.user_wallet.application.port.in.DebitChargingEnergyUseCase;
import com.techmatrix18.user_wallet.application.port.out.BillingLedgerAuditRepository;
import com.techmatrix18.user_wallet.application.port.out.UserWalletRepository;
import com.techmatrix18.user_wallet.domain.UserWallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DebitChargingEnergyService
 * Реализация бизнес-логики периодического списания за энергию
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class DebitChargingEnergyService implements DebitChargingEnergyUseCase {

    private final UserWalletRepository walletRepository;
    private final BillingLedgerAuditRepository auditRepository;

    public DebitChargingEnergyService(UserWalletRepository walletRepository, BillingLedgerAuditRepository auditRepository) {
        this.walletRepository = walletRepository;
        this.auditRepository = auditRepository;
    }

    @Override
    @Transactional
    public void debitChargingEnergy(DebitChargingEnergyCommand command) {
        // Извлекаем кошелек пользователя (блокировка сработает на уровне сохранения версии)
        UserWallet wallet = walletRepository.findByUserId(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user ID: " + command.userId()));

        // Списываем сумму за этот тик внутри доменной сущности (проверяются правила баланса)
        wallet.withdraw(command.amount());

        // Сохраняем обновленный кошелек в базу данных
        walletRepository.save(wallet);

        // Делаем проводку в append-only аудит-лог (заполняем поля из миграции №12)
        // Передаем дельту кВт*ч для возможности проведения перерасчетов и энерго-аудита
        String auditComment = String.format("Charged for delta: %s kWh. Session ID: %d",
                command.kwhDelta().toString(), command.sessionId());

        auditRepository.logOperation(
                command.userId(),
                command.sessionId(),
                "PERIODIC_ENERGY_DEBIT",
                command.amount().negate(), // В лог биллинга списания идут со знаком минус
                wallet.getBalance(),       // Фиксируем snapshot баланса ПОСЛЕ операции
                auditComment
        );
    }
}

