package com.techmatrix18.building_blocks.infrastructure.interceptors;

import com.techmatrix18.building_blocks.infrastructure.db.IdempotencyRecordEntity;
import com.techmatrix18.building_blocks.infrastructure.db.IdempotencyRecordEntity.IdempotencyStatus;
import com.techmatrix18.building_blocks.infrastructure.db.JpaIdempotencyRecordRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * IdempotencyInterceptor
 * Автономный перехватчик HTTP-запросов для проверки заголовка X-Idempotency-Key (Слой технической infrastructure)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 02.09.2026
 */

@Component
public class IdempotencyInterceptor implements HandlerInterceptor {

    private static final String IDEMPOTENCY_HEADER = "X-Idempotency-Key";
    private static final String ATTRIBUTE_KEY_NAME = "CURRENT_IDEMPOTENCY_KEY";

    private final JpaIdempotencyRecordRepository repository;

    public IdempotencyInterceptor(JpaIdempotencyRecordRepository repository) {
        this.repository = repository;
    }

    /**
     * Перехватывает входящий HTTP-запрос до его передачи в метод контроллера.
     * Проверяет наличие маркерной аннотации {@link RequireIdempotency}.
     * Выполняет валидацию заголовка X-Idempotency-Key, вычисляет хэш параметров
     * и сопоставляет данные с историей транзакций в таблице идемпотентности.
     *
     * @param request  Текущий входящий HTTP-запрос
     * @param response Текущий формируемый HTTP-ответ
     * @param handler  Целевой объект обработчика (метод контроллера)
     * @return true, если запрос уникален и его можно передать в Use Case;
     *         false, если запрос заблокирован (дубликат или конфликт данных)
     * @throws Exception В случае сетевых сбоев или ошибок ввода-вывода при записи ответа
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Если это не метод контроллера, сразу пропускаем
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // ШАГ 1: Сканируем аннотацию @RequireIdempotency
        if (!handlerMethod.hasMethodAnnotation(RequireIdempotency.class)) {
            return true;
        }

        // ШАГ 2: Извлекаем обязательный X-Idempotency-Key
        String key = request.getHeader(IDEMPOTENCY_HEADER);
        if (key == null || key.trim().isEmpty()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing required " + IDEMPOTENCY_HEADER + " header.");
            return false; // Запрос отсечен, контроллер не вызывается
        }

        // ШАГ 3: Вычисляем хэш на основе URI и параметров (альтернатива чтению InputStream, не требующая фильтров)
        String requestTarget = request.getRequestURI() + "?" + (request.getQueryString() != null ? request.getQueryString() : "");
        String payloadHash = calculateSha256(requestTarget.getBytes(StandardCharsets.UTF_8));

        // ШАГ 4: Поиск ключа в базе данных
        // Если есть в БД, то достаем - иначе, добавляем новый запрос в БД со статусом STARTED
        Optional<IdempotencyRecordEntity> existingRecord = repository.findById(key);

        if (existingRecord.isPresent()) {
            IdempotencyRecordEntity record = existingRecord.get();

            // Если URL/Параметры или хэш не совпадают с оригинальным запросом
            // Попытка подмены данных (Атака или сбой фронтенда)
            if (!record.getRequestPayloadHash().equals(payloadHash)) {
                response.sendError(HttpStatus.CONFLICT.value(), "Idempotency key conflict: Target endpoint or parameters do not match.");
                return false; // Запрос отсечен, контроллер не вызывается
            }

            // Если транзакция выполняется прямо сейчас в другом потоке
            // Отсекаю повторный клик
            if (record.getStatus() == IdempotencyStatus.STARTED || record.getStatus() == IdempotencyStatus.PROCESSING) {
                response.sendError(HttpStatus.LOCKED.value(), "Request with this key is already being processed.");
                return false; // Запрос отсечен, контроллер не вызывается
            }

            // Если операция завершена успешно — отдаем старый закешированный ответ
            if (record.getStatus() == IdempotencyStatus.COMPLETED) {
                response.setStatus(record.getResponseCode());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(record.getResponseBody());
                return false; // Запрос отсечен, контроллер не вызывается, а клиент мгновенно получает свой старый успешный ответ
            }
        }

        // ШАГ 5: Регистрация транзакции в БД
        IdempotencyRecordEntity newRecord = IdempotencyRecordEntity.createNew(key, payloadHash);
        newRecord.setStatus(IdempotencyStatus.STARTED);
        repository.save(newRecord);

        // Сохраняем токен в контексте запроса для метода afterCompletion
        request.setAttribute(ATTRIBUTE_KEY_NAME, key);

        return true;
    }

    /**
     * Вызывается автоматически после полного завершения обработки запроса и рендеринга представления.
     * Фиксирует финальный результат операции в таблице идемпотентности.
     * Если запрос упал с ошибкой сервера (исключение или код >= 500), переводит запись в FAILED для возможности повтора.
     * В случае успешного исхода кэширует HTTP-статус ответа для последующих дубликатов.
     *
     * @param request  Текущий HTTP-запрос
     * @param response Текущий HTTP-ответ
     * @param handler  Объект обработчика (контроллера)
     * @param ex       Исключение, возникшее при обработке запроса (null, если обработка прошла без ошибок)
     * @throws Exception В случае ошибок при работе с базой данных
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String key = (String) request.getAttribute(ATTRIBUTE_KEY_NAME);
        if (key == null) {
            return;
        }

        Optional<IdempotencyRecordEntity> existingRecord = repository.findById(key);
        if (existingRecord.isEmpty()) {
            return;
        }

        IdempotencyRecordEntity record = existingRecord.get();

        // ШАГ 6: Закрываем статус транзакции на выходе
        if (ex != null || response.getStatus() >= 500) {
            record.setStatus(IdempotencyStatus.FAILED);
        } else {
            record.setStatus(IdempotencyStatus.COMPLETED);
            record.setResponseCode(response.getStatus());
            record.setResponseBody("{}"); // Сюда можно передавать сериализованный ответ
        }

        repository.save(record);
    }

    /**
     * Вычисляет криптографический хэш SHA-256 для переданного массива байт.
     * Результат возвращается в виде шестнадцатеричной строки (Hex String) длиной 64 символа.
     * Используется для защиты от подмены тела (payload) или параметров повторного запроса.
     *
     * @param bytes Исходный массив байт для хэширования
     * @return Строка хэша в нижнем регистре (64 символа Hex)
     * @throws NoSuchAlgorithmException Если алгоритм SHA-256 не поддерживается текущей JVM
     */
    private String calculateSha256(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(bytes);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

