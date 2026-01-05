package com.earthguard.earthquake.dto.preference;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceRequest {

    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private Double preferredLatitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private Double preferredLongitude;

    @Min(value = 1, message = "Radius must be at least 1 km")
    private Double radiusKm;

    private String locationName;

    @DecimalMin(value = "0.0", message = "Min magnitude must be >= 0")
    @DecimalMax(value = "10.0", message = "Min magnitude must be <= 10")
    private Double minMagnitude;

    private Boolean emailNotifications;
    private Boolean pushNotifications;
    private Boolean criticalOnly;
}