package com.earthguard.earthquake.messaging;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.common.enums.AlertLevel;
import com.earthguard.earthquake.config.KafkaTopicConfig;
import com.earthguard.earthquake.dto.EarthquakeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EarthquakeEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publishes earthquake event to Kafka AFTER the current transaction commits.
     * This prevents sending events for data that gets rolled back.
     */
    public void sendEarthquakeEvent(Earthquake earthquake, String eventType) {
        EarthquakeEvent event = EarthquakeEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .earthquakeId(earthquake.getId())
                .magnitude(earthquake.getMagnitude())
                .latitude(earthquake.getLatitude())
                .longitude(earthquake.getLongitude())
                .location(earthquake.getLocation())
                .timestamp(earthquake.getTimestamp())
                .alertLevel(earthquake.getAlertLevel())
                .eventType(eventType)
                .eventTimestamp(LocalDateTime.now())
                .build();

        String topic = determineTopic(earthquake);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishEvent(topic, earthquake.getId(), event);
                }
            });
        } else {
            publishEvent(topic, earthquake.getId(), event);
        }
    }

    private void publishEvent(String topic, String key, EarthquakeEvent event) {
        log.info("Publishing event: id={}, magnitude={}, topic={}", event.getEarthquakeId(), event.getMagnitude(), topic);

        kafkaTemplate.send(topic, key, event).whenComplete((result, ex) -> {
            if (ex == null) {
                log.debug("Event published: topic={}, partition={}, offset={}",
                        topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish event {}: {}", event.getEarthquakeId(), ex.getMessage());
            }
        });
    }

    private String determineTopic(Earthquake earthquake) {
        AlertLevel level = earthquake.getAlertLevel();
        if (level == AlertLevel.CRITICAL || level == AlertLevel.HIGH) {
            return KafkaTopicConfig.CRITICAL_ALERTS_TOPIC;
        }
        return KafkaTopicConfig.EARTHQUAKE_EVENTS_TOPIC;
    }
}
