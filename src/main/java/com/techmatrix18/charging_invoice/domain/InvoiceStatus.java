package com.techmatrix18.charging_invoice.domain;

/**
 * InvoiceStatus
 * Перечисление финансовых состояний бухгалтерского счета
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public enum InvoiceStatus {
    ISSUED,   // Выставлен водителю
    PAID,     // Успешно оплачен с кошелька
    FAILED,   // Ошибка транзакции / Овердрафт баланса
    DISPUTED  // Оспорен пользователем в чате поддержки
}

