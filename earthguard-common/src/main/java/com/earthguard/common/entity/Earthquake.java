package com.earthguard.common.entity;

import com.earthguard.common.enums.AlertLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "earthquakes", indexes = {
        @Index(name = "idx_magnitude", columnList = "magnitude"),
        @Index(name = "idx_timestamp", columnList = "timestamp"),
        @Index(name = "idx_location", columnList = "latitude,longitude")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Earthquake {

    @Id
    @Column(length = 100)
    private String id; // Unique ID that comes from USGS API (ex: "us6000m6p5")

    @Column(nullable = false)
    private Double magnitude; // Magnitude (ex: 5.8)

    @Column(nullable = false)
    private Double latitude; // Latitude (between -90 and +90)

    @Column(nullable = false)
    private Double longitude; // Longitude (between -180 and +180 )

    private Integer depth; // Depth (km)

    @Column(length = 500)
    private String location; // İnsan okunabilir konum (ex: "23 km NE of Istanbul, Turkey")

    @Column(nullable = false)
    private LocalDateTime timestamp; // Time of earthquake

    @Column(length = 1000)
    private String url; // Detail page of USGS

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AlertLevel alertLevel; // Alert level (enum)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // Data creation time

    private LocalDateTime updatedAt; // Last update time

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (alertLevel == null && magnitude != null) {
            alertLevel = AlertLevel.fromMagnitude(magnitude);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (magnitude != null) {
            alertLevel = AlertLevel.fromMagnitude(magnitude);
        }
    }
}