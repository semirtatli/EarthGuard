package com.earthguard.notification.service;

import com.earthguard.notification.dto.EarthquakeEvent;
import com.earthguard.notification.service.email.EmailService;
import com.earthguard.notification.service.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;
    private final WebSocketService webSocketService;

    @Value("${earthguard.notification.default-recipients:admin@earthguard.com}")
    private String[] defaultRecipients;

    @Override
    public void sendEmailNotification(EarthquakeEvent event) {
        log.info("📧 Processing email notification for earthquake: {}", event.getEarthquakeId());

        for (String recipient : defaultRecipients) {
            emailService.sendEarthquakeAlert(event, recipient);
        }

        log.info("📧 Email notification processed for {} recipients", defaultRecipients.length);
    }

    @Override
    public void sendWebSocketNotification(EarthquakeEvent event) {
        log.info("🔔 Processing WebSocket notification for earthquake: {}", event.getEarthquakeId());

        webSocketService.broadcastEarthquakeAlert(event);

        log.info("🔔 WebSocket notification completed");
    }
}