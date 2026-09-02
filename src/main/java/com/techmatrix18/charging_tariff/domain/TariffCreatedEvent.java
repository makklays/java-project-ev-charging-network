package com.techmatrix18.charging_tariff.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * TariffCreatedEvent
 * Доменное событие заведения новой тарифной сетки.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

public class TariffCreatedEvent {
    private final Long tariffId;
    private final BigDecimal pricePerKwh;
    private final ZonedDateTime timestamp;

    public TariffCreatedEvent(Long tariffId, BigDecimal pricePerKwh) {
        this.tariffId = tariffId;
        this.pricePerKwh = pricePerKwh;
        this.timestamp = ZonedDateTime.now();
    }

    public Long getTariffId() { return tariffId; }
    public BigDecimal getPricePerKwh() { return pricePerKwh; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}

