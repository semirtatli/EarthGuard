package com.earthguard.notification.service;

import com.earthguard.notification.dto.EarthquakeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendEmailNotification(EarthquakeEvent event) {
        // Mock implementation for now
        log.info("📧 EMAIL NOTIFICATION: Earthquake detected!");
        log.info("   ID: {}", event.getEarthquakeId());
        log.info("   Magnitude: {}", event.getMagnitude());
        log.info("   Location: {}", event.getLocation());
        log.info("   Alert Level: {}", event.getAlertLevel());
        log.info("   Time: {}", event.getTimestamp());

        // TODO: Real email implementation with Spring Mail
        // For now, just logging
    }

    @Override
    public void sendWebSocketNotification(EarthquakeEvent event) {
        // Mock implementation for now
        log.info("🔔 WEBSOCKET NOTIFICATION: Real-time alert sent!");
        log.info("   Event: {}", event.getEventType());
        log.info("   Magnitude: {} at {}", event.getMagnitude(), event.getLocation());

        // TODO: Real WebSocket implementation
        // For now, just logging
    }
}