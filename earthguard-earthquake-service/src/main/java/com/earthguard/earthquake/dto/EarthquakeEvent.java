package com.earthguard.earthquake.dto;

import com.earthguard.common.enums.AlertLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EarthquakeEvent {
    private String eventId;
    private String earthquakeId;
    private Double magnitude;
    private Double latitude;
    private Double longitude;
    private String location;
    private LocalDateTime timestamp;
    private AlertLevel alertLevel;
    private String eventType; // CREATED, UPDATED, DELETED
    private LocalDateTime eventTimestamp;
}