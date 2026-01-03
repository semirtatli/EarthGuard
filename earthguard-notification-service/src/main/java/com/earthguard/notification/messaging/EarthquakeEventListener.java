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

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "earthquake-events",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleEarthquakeEvent(EarthquakeEvent event) {
        log.info("=== RECEIVED EARTHQUAKE EVENT ===");
        log.info("Event ID: {}", event.getEventId());
        log.info("Earthquake ID: {}", event.getEarthquakeId());
        log.info("Magnitude: {}", event.getMagnitude());
        log.info("Location: {}", event.getLocation());
        log.info("Alert Level: {}", event.getAlertLevel());
        log.info("=================================");

        try {
            // Send notifications
            notificationService.sendWebSocketNotification(event);

            // Only send email for significant earthquakes (magnitude >= 5.0)
            if (event.getMagnitude() != null && event.getMagnitude() >= 5.0) {
                notificationService.sendEmailNotification(event);
            }

            log.info("Notifications processed successfully for earthquake: {}", event.getEarthquakeId());

        } catch (Exception e) {
            log.error("Error processing earthquake event: {}", event.getEarthquakeId(), e);
        }
    }

    @KafkaListener(
            topics = "critical-alerts",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleCriticalAlert(EarthquakeEvent event) {
        log.warn("=== CRITICAL ALERT RECEIVED ===");
        log.warn("🚨 CRITICAL EARTHQUAKE DETECTED! 🚨");
        log.warn("Event ID: {}", event.getEventId());
        log.warn("Earthquake ID: {}", event.getEarthquakeId());
        log.warn("Magnitude: {}", event.getMagnitude());
        log.warn("Location: {}", event.getLocation());
        log.warn("Alert Level: {}", event.getAlertLevel());
        log.warn("================================");

        try {
            // Always send both notifications for critical earthquakes
            notificationService.sendEmailNotification(event);
            notificationService.sendWebSocketNotification(event);

            // TODO: Additional critical alert actions
            // - SMS notifications
            // - Push notifications
            // - Alert authorities

            log.warn("Critical alert notifications sent for: {}", event.getEarthquakeId());

        } catch (Exception e) {
            log.error("Error processing critical alert: {}", event.getEarthquakeId(), e);
        }
    }
}