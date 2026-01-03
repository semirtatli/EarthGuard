package com.earthguard.earthquake.messaging;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.earthquake.config.KafkaTopicConfig;
import com.earthguard.earthquake.dto.EarthquakeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EarthquakeEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

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

        String topic = determineTopicByAlertLevel(earthquake);

        log.info("Publishing earthquake event: id={}, magnitude={}, alertLevel={}, topic={}",
                earthquake.getId(), earthquake.getMagnitude(), earthquake.getAlertLevel(), topic);

        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topic, earthquake.getId(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Successfully published event to Kafka: topic={}, partition={}, offset={}",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish event to Kafka: {}", ex.getMessage(), ex);
            }
        });
    }

    private String determineTopicByAlertLevel(Earthquake earthquake) {
        // Critical earthquakes (magnitude >= 6.0) go to critical-alerts topic
        if (earthquake.getMagnitude() != null && earthquake.getMagnitude() >= 6.0) {
            return KafkaTopicConfig.CRITICAL_ALERTS_TOPIC;
        }
        // All others go to general earthquake-events topic
        return KafkaTopicConfig.EARTHQUAKE_EVENTS_TOPIC;
    }
}