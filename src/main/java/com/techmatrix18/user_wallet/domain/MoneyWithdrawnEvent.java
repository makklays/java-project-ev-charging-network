package com.techmatrix18.user_wallet.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * MoneyWithdrawnEvent
 * Доменное событие успешного вывода средств из кошелька на банковскую карту.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class MoneyWithdrawnEvent {
    private final UUID walletId;
    private final Long userId;
    private final BigDecimal amount;
    private final ZonedDateTime occurredAt;

    public MoneyWithdrawnEvent(UUID walletId, Long userId, BigDecimal amount) {
        this.walletId = walletId;
        this.userId = userId;
        this.amount = amount;
        this.occurredAt = ZonedDateTime.now();
    }

    public UUID getWalletId() { return walletId; }
    public Long getUserId() { return userId; }
    public BigDecimal getAmount() { return amount; }
    public ZonedDateTime getOccurredAt() { return occurredAt; }
}

