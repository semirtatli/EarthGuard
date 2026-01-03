package com.earthguard.notification.dto.websocket;

import com.earthguard.notification.dto.EarthquakeEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    private String messageId;
    private String type; // EARTHQUAKE, CRITICAL_ALERT, INFO
    private String title;
    private String message;
    private EarthquakeEvent earthquakeData;
    private LocalDateTime timestamp;
    private String severity; // INFO, WARNING, ERROR, CRITICAL
}