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

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastEarthquakeAlert(EarthquakeEvent event) {
        WebSocketMessage message = createWebSocketMessage(event);

        String destination = determineDestination(event);

        log.info("🔔 Broadcasting WebSocket message to {}", destination);
        log.debug("Message: {}", message);

        messagingTemplate.convertAndSend(destination, message);

        log.info("✅ WebSocket broadcast completed");
    }

    private WebSocketMessage createWebSocketMessage(EarthquakeEvent event) {
        String type = event.getMagnitude() >= 6.0 ? "CRITICAL_ALERT" : "EARTHQUAKE";
        String severity = determineSeverity(event.getMagnitude());
        String title = createTitle(event);
        String message = createMessage(event);

        return WebSocketMessage.builder()
                .messageId(UUID.randomUUID().toString())
                .type(type)
                .title(title)
                .message(message)
                .earthquakeData(event)
                .timestamp(LocalDateTime.now())
                .severity(severity)
                .build();
    }

    private String determineDestination(EarthquakeEvent event) {
        if (event.getMagnitude() >= 6.0) {
            return "/topic/critical-alerts";
        }
        return "/topic/earthquakes";
    }

    private String determineSeverity(Double magnitude) {
        if (magnitude >= 7.0) return "CRITICAL";
        if (magnitude >= 6.0) return "ERROR";
        if (magnitude >= 5.0) return "WARNING";
        return "INFO";
    }

    private String createTitle(EarthquakeEvent event) {
        if (event.getMagnitude() >= 7.0) {
            return "🚨 CRITICAL EARTHQUAKE ALERT";
        } else if (event.getMagnitude() >= 6.0) {
            return "⚠️ HIGH MAGNITUDE EARTHQUAKE";
        } else if (event.getMagnitude() >= 5.0) {
            return "📢 Earthquake Detected";
        }
        return "ℹ️ Seismic Activity";
    }

    private String createMessage(EarthquakeEvent event) {
        return String.format(
                "Magnitude %.1f earthquake detected at %s. Location: %.4f°, %.4f°",
                event.getMagnitude(),
                event.getLocation(),
                event.getLatitude(),
                event.getLongitude()
        );
    }
}