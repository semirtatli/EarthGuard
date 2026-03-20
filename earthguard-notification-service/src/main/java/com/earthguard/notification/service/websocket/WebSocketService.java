package com.earthguard.notification.service.websocket;

import com.earthguard.notification.dto.EarthquakeEvent;
import com.earthguard.notification.dto.websocket.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

    private static final double CRITICAL_THRESHOLD = 7.0;
    private static final double HIGH_THRESHOLD = 6.0;
    private static final double MODERATE_THRESHOLD = 5.0;

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastEarthquakeAlert(EarthquakeEvent event) {
        WebSocketMessage message = createMessage(event);
        String destination = event.getMagnitude() >= HIGH_THRESHOLD
                ? "/topic/critical-alerts"
                : "/topic/earthquakes";

        log.info("Broadcasting to {}: magnitude={}", destination, event.getMagnitude());
        messagingTemplate.convertAndSend(destination, message);
    }

    private WebSocketMessage createMessage(EarthquakeEvent event) {
        double mag = event.getMagnitude();
        return WebSocketMessage.builder()
                .messageId(UUID.randomUUID().toString())
                .type(mag >= HIGH_THRESHOLD ? "CRITICAL_ALERT" : "EARTHQUAKE")
                .title(getTitle(mag))
                .message(String.format("Magnitude %.1f earthquake at %s (%.4f, %.4f)",
                        mag, event.getLocation(), event.getLatitude(), event.getLongitude()))
                .earthquakeData(event)
                .timestamp(LocalDateTime.now())
                .severity(getSeverity(mag))
                .build();
    }

    private String getTitle(double magnitude) {
        if (magnitude >= CRITICAL_THRESHOLD) return "CRITICAL EARTHQUAKE ALERT";
        if (magnitude >= HIGH_THRESHOLD) return "HIGH MAGNITUDE EARTHQUAKE";
        if (magnitude >= MODERATE_THRESHOLD) return "Earthquake Detected";
        return "Seismic Activity";
    }

    private String getSeverity(double magnitude) {
        if (magnitude >= CRITICAL_THRESHOLD) return "CRITICAL";
        if (magnitude >= HIGH_THRESHOLD) return "ERROR";
        if (magnitude >= MODERATE_THRESHOLD) return "WARNING";
        return "INFO";
    }
}
