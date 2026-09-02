package com.techmatrix18.building_blocks.infrastructure.config;

import com.techmatrix18.building_blocks.infrastructure.interceptors.IdempotencyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfig
 * Единственный конфигурационный класс для подключения интерцептора (Слой технической инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final IdempotencyInterceptor idempotencyInterceptor;

    // Spring сам найдет IdempotencyInterceptor благодаря аннотации @Component над ним
    public WebMvcConfig(IdempotencyInterceptor idempotencyInterceptor) {
        this.idempotencyInterceptor = idempotencyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Разрешаю интерцептору сканировать входящие запросы.
        // Он будет включать защиту ТОЛЬКО там, где выборочно поставите @RequireIdempotency (в методах контроллеров).
        registry.addInterceptor(idempotencyInterceptor).addPathPatterns("/**");
    }
}

