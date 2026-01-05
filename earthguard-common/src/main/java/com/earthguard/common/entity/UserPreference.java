package com.earthguard.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Location preferences
    @Column(name = "preferred_latitude")
    private Double preferredLatitude;

    @Column(name = "preferred_longitude")
    private Double preferredLongitude;

    @Column(name = "radius_km")
    private Double radiusKm; // Alert radius in kilometers

    @Column(length = 200)
    private String locationName; // Human-readable (e.g., "Istanbul, Turkey")

    // Magnitude filter
    @Column(name = "min_magnitude")
    private Double minMagnitude; // Only alert for earthquakes >= this magnitude

    // Notification preferences
    @Column(name = "email_notifications")
    private Boolean emailNotifications = true;

    @Column(name = "push_notifications")
    private Boolean pushNotifications = true;

    @Column(name = "critical_only")
    private Boolean criticalOnly = false; // Only notify for critical alerts (magnitude >= 6.0)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}