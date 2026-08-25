package com.techmatrix18.user.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * MobileValidator
 * Реализация валидатора для проверки строки на соответствие формату телефона
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public class MobileValidator implements ConstraintValidator<ValidMobile, String> {

    // Регулярное выражение для проверки международного формата:
    // Обязательный знак +, код страны, и от 10 до 14 цифр суммарно.
    private static final String PHONE_PATTERN = "^\\+\\d{10,14}$";

    @Override
    public boolean isValid(String mobileField, ConstraintValidatorContext context) {
        if (mobileField == null) {
            return true; // Позволяем @NotNull самостоятельно решать судьбу null-значений
        }
        return mobileField.matches(PHONE_PATTERN);
    }
}

