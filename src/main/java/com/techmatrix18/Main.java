package com.techmatrix18;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main
 * Главная точка входа и запуска распределенной платформы EV Charging Network.
 * Активирует автоконфигурацию Spring Boot и фоновые шедулеры Outbox-паблишера.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 17.08.2026
 */

@SpringBootApplication
@EnableScheduling
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello EV Charging network world!");

        // Start Spring Boot
        SpringApplication.run(Main.class, args);
    }
}

