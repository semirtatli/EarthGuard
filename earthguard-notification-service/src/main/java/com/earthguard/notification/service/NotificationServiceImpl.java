package com.earthguard.notification.service;

import com.earthguard.notification.dto.EarthquakeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.earthguard.notification.service.email.EmailService;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;

    @Value("${earthguard.notification.default-recipients:admin@earthguard.com}")
    private String[] defaultRecipients;

    @Override
    public void sendEmailNotification(EarthquakeEvent event) {
        log.info("📧 Processing email notification for earthquake: {}", event.getEarthquakeId());

        // Send to default recipients
        for (String recipient : defaultRecipients) {
            emailService.sendEarthquakeAlert(event, recipient);
        }

        log.info("📧 Email notification processed for {} recipients", defaultRecipients.length);
    }

    @Override
    public void sendWebSocketNotification(EarthquakeEvent event) {
        // Mock implementation for now (WebSocket next)
        log.info("🔔 WEBSOCKET NOTIFICATION: Real-time alert sent!");
        log.info("   Event: {}", event.getEventType());
        log.info("   Magnitude: {} at {}", event.getMagnitude(), event.getLocation());

        // TODO: Real WebSocket implementation in next commit
    }
}