package com.earthguard.notification.controller;

import com.earthguard.notification.dto.EarthquakeEvent;
import com.earthguard.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notifications/test")
@RequiredArgsConstructor
@Slf4j
public class NotificationTestController {

    private final NotificationService notificationService;

    @PostMapping("/websocket")
    public ResponseEntity<String> testWebSocket(
            @RequestParam(defaultValue = "5.5") Double magnitude) {

        log.info("Testing WebSocket with magnitude: {}", magnitude);

        EarthquakeEvent testEvent = EarthquakeEvent.builder()
                .eventId("test-ws-001")
                .earthquakeId("test-eq-001")
                .magnitude(magnitude)
                .latitude(41.0)
                .longitude(29.0)
                .location("Test Location")
                .timestamp(LocalDateTime.now())
                .alertLevel(magnitude >= 6.0 ? "CRITICAL" : "MODERATE")
                .eventType("TEST")
                .eventTimestamp(LocalDateTime.now())
                .build();

        notificationService.sendWebSocketNotification(testEvent);

        return ResponseEntity.ok("WebSocket test message sent!");
    }
}