package com.techmatrix18.charging_tariff.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * TariffPriceChangedEvent
 * Доменное событие изменения (модернизации) стоимостных характеристик тарифа.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class TariffPriceChangedEvent {
    private final Long tariffId;
    private final BigDecimal oldPrice;
    private final BigDecimal newPrice;
    private final ZonedDateTime timestamp;

    public TariffPriceChangedEvent(Long tariffId, BigDecimal oldPrice, BigDecimal newPrice) {
        this.tariffId = tariffId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.timestamp = ZonedDateTime.now();
    }

    public Long getTariffId() { return tariffId; }
    public BigDecimal getOldPrice() { return oldPrice; }
    public BigDecimal getNewPrice() { return newPrice; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}

