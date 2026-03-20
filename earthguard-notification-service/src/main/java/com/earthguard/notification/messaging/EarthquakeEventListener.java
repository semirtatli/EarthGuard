package com.earthguard.notification.messaging;

import com.earthguard.notification.dto.EarthquakeEvent;
import com.earthguard.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EarthquakeEventListener {

    private static final double EMAIL_THRESHOLD = 5.0;

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "earthquake-events",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleEarthquakeEvent(EarthquakeEvent event) {
        log.info("Received earthquake event: id={}, magnitude={}, location={}",
                event.getEarthquakeId(), event.getMagnitude(), event.getLocation());

        try {
            notificationService.sendWebSocketNotification(event);

            if (event.getMagnitude() != null && event.getMagnitude() >= EMAIL_THRESHOLD) {
                notificationService.sendEmailNotification(event);
            }
        } catch (Exception e) {
            log.error("Error processing earthquake event {}: {}", event.getEarthquakeId(), e.getMessage(), e);
        }
    }

    @KafkaListener(
            topics = "critical-alerts",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleCriticalAlert(EarthquakeEvent event) {
        log.warn("CRITICAL ALERT: id={}, magnitude={}, location={}",
                event.getEarthquakeId(), event.getMagnitude(), event.getLocation());

        try {
            notificationService.sendEmailNotification(event);
            notificationService.sendWebSocketNotification(event);
        } catch (Exception e) {
            log.error("Error processing critical alert {}: {}", event.getEarthquakeId(), e.getMessage(), e);
        }
    }
}
