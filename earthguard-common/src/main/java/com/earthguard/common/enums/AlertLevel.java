package com.earthguard.common.enums;

public enum AlertLevel {
    NONE(0.0, 4.0, "No alert"),
    LOW(4.0, 5.0, "Minor earthquake"),
    MODERATE(5.0, 6.0, "Moderate earthquake"),
    HIGH(6.0, 7.0, "Strong earthquake"),
    CRITICAL(7.0, Double.MAX_VALUE, "Major earthquake");

    private final Double minMagnitude;
    private final Double maxMagnitude;
    private final String description;

    AlertLevel(Double minMagnitude, Double maxMagnitude, String description) {
        this.minMagnitude = minMagnitude;
        this.maxMagnitude = maxMagnitude;
        this.description = description;
    }

    public static AlertLevel fromMagnitude(Double magnitude) {
        if (magnitude == null) return NONE;
        for (AlertLevel level : values()) {
            if (magnitude >= level.minMagnitude && magnitude < level.maxMagnitude) {
                return level;
            }
        }
        return CRITICAL;
    }

    public Double getMinMagnitude() { return minMagnitude; }
    public Double getMaxMagnitude() { return maxMagnitude; }
    public String getDescription() { return description; }
}