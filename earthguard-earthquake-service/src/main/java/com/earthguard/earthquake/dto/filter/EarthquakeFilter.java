package com.earthguard.earthquake.dto.filter;

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
public class EarthquakeFilter {

    // Magnitude filters
    private Double minMagnitude;
    private Double maxMagnitude;

    // Date filters
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Location filters
    private String location;
    private Double latitude;
    private Double longitude;
    private Double radiusKm;

    // Alert level filter
    private AlertLevel alertLevel;

    // Depth filter
    private Integer minDepth;
    private Integer maxDepth;
}