package com.earthguard.notification.dto;

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
    private String alertLevel; // String olarak alıyoruz (JSON deserialize kolaylığı)
    private String eventType;
    private LocalDateTime eventTimestamp;
}