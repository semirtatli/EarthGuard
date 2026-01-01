package com.earthguard.earthquake.dto;

import com.earthguard.common.enums.AlertLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EarthquakeResponse {
    private String id;
    private Double magnitude;
    private Double latitude;
    private Double longitude;
    private Integer depth;
    private String location;
    private LocalDateTime timestamp;
    private String url;
    private AlertLevel alertLevel;
    private String alertDescription;
    private LocalDateTime createdAt;
}