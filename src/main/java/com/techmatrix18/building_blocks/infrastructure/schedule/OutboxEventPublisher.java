package com.techmatrix18.building_blocks.infrastructure.schedule;

import com.techmatrix18.building_blocks.infrastructure.db.JpaOutboxEventRepository;
import com.techmatrix18.building_blocks.infrastructure.db.OutboxEventEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * OutboxEventPublisher
 * Фоновый воркер (Message Relay) для пакетной отправки событий из Outbox в Kafka (Слой технической инфраструктуры)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 01.09.2026
 */

@Component
public class OutboxEventPublisher {

    private final JpaOutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxEventPublisher(JpaOutboxEventRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Опрашивает таблицу outbox_events с фиксированной задержкой в 200 миллисекунд после окончания предыдущего запуска.
     * Выбирает события батчами, чтобы не перегружать память СУБД и приложения.
     */
    @Scheduled(fixedDelay = 200)
    @Transactional // Гарантирует обновление статусов в БД в рамках одной транзакции воркера
    public void publishPendingEvents() {

        // Извлекаем первые 50 неотправленных событий, отсортированных по времени создания (FIFO)
        List<OutboxEventEntity> pendingEvents = outboxRepository.findByStatusOrderByCreatedAtAsc(
                OutboxEventEntity.OutboxStatus.PENDING,
                PageRequest.of(0, 50)
        );

        if (pendingEvents.isEmpty()) {
            return;
        }

        for (OutboxEventEntity event : pendingEvents) {
            try {
                // Имя топика формируем на основе типа агрегата (например, "ev-station-events" или "charging-invoice-events")
                String topicName = event.getAggregateType().toLowerCase().replace("_", "-") + "-events";

                // Передаем eventType в заголовки Kafka, чтобы потребители знали, какой класс десериализовать
                String key = event.getAggregateId(); // Идентификатор агрегата — это Partition Key в Kafka
                String payload = event.getPayload();

                // Синхронно или асинхронно отправляем в Kafka с ожиданием подтверждения (acks=all / acks=1)
                CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, key, payload);

                // Блокируем поток до получения подтверждения от кластера Kafka (Acknowledge)
                // Для максимальной надежности воркер должен убедиться, что брокер принял сообщение, прежде чем менять статус в БД
                future.get();

                // Если отправка прошла успешно
                event.setStatus(OutboxEventEntity.OutboxStatus.PROCESSED);
                event.setProcessedAt(ZonedDateTime.now());
                event.setErrorMessage(null);

            } catch (Exception e) {
                // Если брокер упал или сеть моргнула — фиксируем ошибку
                event.setStatus(OutboxEventEntity.OutboxStatus.FAILED);
                event.setErrorMessage(e.getMessage());

                // В реальных высоконагруженных системах здесь также можно инкрементировать счетчик попыток (retry_count),
                // чтобы не блокировать всю очередь из-за одного «битого» сообщения.
            }

            // Обновляем состояние записи в БД
            outboxRepository.save(event);
        }
    }
}

