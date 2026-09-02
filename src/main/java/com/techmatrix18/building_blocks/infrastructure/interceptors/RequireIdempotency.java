package com.techmatrix18.building_blocks.infrastructure.interceptors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RequireIdempotency
 * Аннотация для декларативного включения защиты от повторных запросов (Слой технической инфраструктуры)
 * Ставится над POST/PUT/PATCH методами REST-контроллеров бизнес-модулей.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

@Target(ElementType.METHOD) // Разрешено ставить только над методами контроллеров
@Retention(RetentionPolicy.RUNTIME) // Аннотация должна быть доступна в рантайме для интерцептора
public @interface RequireIdempotency {
    //
}

