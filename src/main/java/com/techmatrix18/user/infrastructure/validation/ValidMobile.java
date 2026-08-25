package com.techmatrix18.user.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ValidMobile
 * Кастомная аннотация для валидации формата номера мобильного телефона
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.08.2026
 */

@Documented
@Constraint(validatedBy = MobileValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMobile {
    String message() default "Неверный формат номера мобильного телефона";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

