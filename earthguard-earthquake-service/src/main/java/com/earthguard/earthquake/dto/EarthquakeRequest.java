package com.earthguard.earthquake.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EarthquakeRequest {

    @NotBlank(message = "ID cannot be blank")
    private String id;

    @NotNull(message = "Magnitude is required")
    @DecimalMin(value = "0.0", message = "Magnitude must be positive")
    @DecimalMax(value = "10.0", message = "Magnitude cannot exceed 10.0")
    private Double magnitude;

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private Double longitude;

    @Min(value = 0, message = "Depth must be positive")
    private Integer depth;

    @Size(max = 500, message = "Location cannot exceed 500 characters")
    private String location;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;

    @Size(max = 1000, message = "URL cannot exceed 1000 characters")
    private String url;
}