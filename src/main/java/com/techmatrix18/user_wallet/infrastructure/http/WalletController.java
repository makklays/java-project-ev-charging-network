package com.techmatrix18.user_wallet.infrastructure.http;

import com.techmatrix18.user_wallet.application.port.in.*;
import com.techmatrix18.user_wallet.application.port.out.UserWalletRepository;
import com.techmatrix18.user_wallet.domain.UserWallet;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * WalletController
 * HTTP-адаптер REST API для управления балансами кошельков сети EV Charging
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final UserWalletRepository walletRepository; // Используется только для безопасного чтения (Queries)
    private final DepositMoneyUseCase depositMoneyUseCase;
    private final DebitChargingEnergyUseCase debitChargingEnergyUseCase;
    private final DebitIdleFeeUseCase debitIdleFeeUseCase;
    private final SettleFinalInvoiceUseCase settleFinalInvoiceUseCase;
    private final WithdrawMoneyUseCase withdrawMoneyUseCase;

    // Внедряем зависимости через конструктор
    public WalletController(
            UserWalletRepository walletRepository,
            DepositMoneyUseCase depositMoneyUseCase,
            DebitChargingEnergyUseCase debitChargingEnergyUseCase,
            DebitIdleFeeUseCase debitIdleFeeUseCase,
            SettleFinalInvoiceUseCase settleFinalInvoiceUseCase,
            WithdrawMoneyUseCase withdrawMoneyUseCase
    ) {
        this.walletRepository = walletRepository;
        this.depositMoneyUseCase = depositMoneyUseCase;
        this.debitChargingEnergyUseCase = debitChargingEnergyUseCase;
        this.debitIdleFeeUseCase = debitIdleFeeUseCase;
        this.settleFinalInvoiceUseCase = settleFinalInvoiceUseCase;
        this.withdrawMoneyUseCase = withdrawMoneyUseCase;
    }

    // Получение текущего баланса кошелька пользователя
    @GetMapping("/users/{userId}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable Long userId) {
        UserWallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user ID: " + userId));

        // Возвращаем аккуратный JSON-ответ с балансом
        return ResponseEntity.ok(Map.of(
                "userId", wallet.getUserId(),
                "balance", wallet.getBalance(),
                "walletId", wallet.getId()
        ));
    }

    // Пополнение баланса кошелька (вызывается после успешного ответа платежной системы)
    @PostMapping("/deposit")
    public ResponseEntity<Void> depositMoney(@Valid @RequestBody DepositMoneyRequest request) {
        // Преобразуем проверенный Request в доменную команду
        depositMoneyUseCase.depositMoney(request.toCommand());
        return ResponseEntity.ok().build(); // HTTP 200 OK
    }

    // Периодическое списание за энергию во время зарядки (вызывается IoT-модулем или Kafka-воркером)
    @PostMapping("/debit-energy")
    public ResponseEntity<Void> debitEnergy(@Valid @RequestBody DebitChargingEnergyRequest request) {
        debitChargingEnergyUseCase.debitChargingEnergy(request.toCommand());
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Списание штрафа за простой (оккупация кабеля после окончания зарядки)
    @PostMapping("/debit-idle")
    public ResponseEntity<Void> debitIdleFee(@Valid @RequestBody DebitIdleFeeRequest request) {
        debitIdleFeeUseCase.debitIdleFee(request.toCommand());
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Финальное закрытие и списание по бухгалтерскому инвойсу сессии
    @PostMapping("/settle-invoice")
    public ResponseEntity<Void> settleInvoice(@Valid @RequestBody SettleFinalInvoiceRequest request) {
        settleFinalInvoiceUseCase.settleFinalInvoice(request.toCommand());
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Вывод (возврат) средств из кошелька на банковскую карту пользователя
    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdrawMoney(@Valid @RequestBody WithdrawMoneyRequest request) {
        withdrawMoneyUseCase.withdrawMoney(request.toCommand());
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}

