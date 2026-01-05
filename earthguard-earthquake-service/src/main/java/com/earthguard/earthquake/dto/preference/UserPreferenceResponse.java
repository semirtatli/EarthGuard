package com.earthguard.earthquake.dto.preference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceResponse {
    private String id;
    private Double preferredLatitude;
    private Double preferredLongitude;
    private Double radiusKm;
    private String locationName;
    private Double minMagnitude;
    private Boolean emailNotifications;
    private Boolean pushNotifications;
    private Boolean criticalOnly;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}