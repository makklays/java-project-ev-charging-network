package com.techmatrix18.user_wallet.application.service;

import com.techmatrix18.user_wallet.application.command.CreateWalletCommand;
import com.techmatrix18.user_wallet.application.port.in.CreateWalletUseCase;
import com.techmatrix18.user_wallet.application.port.out.UserWalletRepository;
import com.techmatrix18.user_wallet.domain.UserWallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CreateWalletService
 * Реализация бизнес-логики создания кошелька пользователя
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 26.08.2026
 */

@Service
public class CreateWalletService implements CreateWalletUseCase {

    private final UserWalletRepository walletRepository;

    public CreateWalletService(UserWalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public void createWallet(CreateWalletCommand command) {
        // Бизнес-проверка: у пользователя не должно быть второго кошелька
        if (walletRepository.existsByUserId(command.userId())) {
            throw new IllegalStateException("Wallet already exists for user ID: " + command.userId());
        }

        // Создаем чистую доменную сущность кошелька (вызовется конструктор бизнес-регистрации с 0.0000 балансом)
        UserWallet newWallet = new UserWallet(command.userId());

        // Сохраняем кошелек через выходной порт в инфраструктуру
        walletRepository.save(newWallet);
    }
}

