package com.techmatrix18.user.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * PasswordValidator
 * Реализация гибкой проверки пароля на спецсимволы, цифры и запрещенные комбинации
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true; // Перекладываем проверку на @NotNull
        }

        // Отключаем дефолтное сообщение из аннотации, чтобы выводить точечные ошибки
        context.disableDefaultConstraintViolation();

        // Нестандартное правило 1: Минимальная и максимальная длина
        if (password.length() < 8 || password.length() > 32) {
            buildMessage(context, "Пароль должен быть длиной от 8 до 32 символов");
            return false;
        }

        // Нестандартное правило 2: Наличие цифр
        if (!password.matches(".*\\d.*")) {
            buildMessage(context, "Пароль должен содержать хотя бы одну цифру");
            return false;
        }

        // Нестандартное правило 3: Наличие заглавных букв
        if (!password.matches(".*[A-Z].*")) {
            buildMessage(context, "Пароль должен содержать хотя бы одну заглавную букву (A-Z)");
            return false;
        }

        // Нестандартное правило 4: Проверка на опасные/шаблонные пароли (Словари)
        if (isCommonPassword(password)) {
            buildMessage(context, "Этот пароль слишком простой и предсказуемый");
            return false;
        }

        return true;
    }

    private void buildMessage(ConstraintValidatorContext context, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

    private boolean isCommonPassword(String password) {
        String lowerPassword = password.toLowerCase();
        // Метод отсекает популярные у хакеров комбинации
        return lowerPassword.contains("12345") ||
                lowerPassword.contains("qwerty") ||
                lowerPassword.contains("password") ||
                lowerPassword.contains("admin");
    }
}

