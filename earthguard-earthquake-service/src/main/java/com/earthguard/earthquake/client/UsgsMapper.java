package com.earthguard.earthquake.client;

import com.earthguard.common.entity.Earthquake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class UsgsMapper {

    public Earthquake toEarthquake(UsgsEarthquakeResponse.Feature feature) {
        try {
            // Validation: required fields
            if (feature == null || feature.getId() == null ||
                    feature.getProperties() == null || feature.getGeometry() == null) {
                log.warn("Skipping feature with missing required fields");
                return null;
            }

            var props = feature.getProperties();
            var coords = feature.getGeometry().getCoordinates();

            // Validate magnitude and coordinates
            if (props.getMag() == null || coords == null || coords.size() < 2) {
                log.warn("Skipping feature {} - missing magnitude or coordinates", feature.getId());
                return null;
            }

            Earthquake earthquake = new Earthquake();

            // ID (clean it - remove network prefix if exists)
            earthquake.setId(cleanId(feature.getId()));

            // Magnitude
            earthquake.setMagnitude(props.getMag());

            // Location (fallback to "Unknown Location" if null)
            earthquake.setLocation(
                    props.getPlace() != null ? props.getPlace() : "Unknown Location"
            );

            // URL
            earthquake.setUrl(props.getUrl());

            // Timestamp (convert from milliseconds since epoch)
            earthquake.setTimestamp(convertTimestamp(props.getTime()));

            // Coordinates: [longitude, latitude, depth]
            earthquake.setLongitude(coords.get(0));
            earthquake.setLatitude(coords.get(1));

            // Depth (can be null)
            if (coords.size() >= 3 && coords.get(2) != null) {
                earthquake.setDepth(coords.get(2).intValue());
            }

            log.debug("Successfully mapped earthquake: id={}, mag={}, location={}",
                    earthquake.getId(), earthquake.getMagnitude(), earthquake.getLocation());

            return earthquake;

        } catch (Exception e) {
            log.error("Error mapping USGS feature to Earthquake: {}",
                    feature != null ? feature.getId() : "null", e);
            return null;
        }
    }

    public List<Earthquake> toEarthquakes(UsgsEarthquakeResponse response) {
        if (response == null || response.getFeatures() == null) {
            log.warn("USGS response is null or has no features");
            return List.of();
        }

        log.info("Mapping {} features from USGS response", response.getFeatures().size());

        List<Earthquake> earthquakes = response.getFeatures().stream()
                .map(this::toEarthquake)
                .filter(Objects::nonNull)
                .toList();

        log.info("Successfully mapped {} earthquakes (filtered {} invalid)",
                earthquakes.size(),
                response.getFeatures().size() - earthquakes.size());

        return earthquakes;
    }

    /**
     * Convert USGS timestamp (milliseconds since epoch) to LocalDateTime
     */
    private LocalDateTime convertTimestamp(Long timeMillis) {
        if (timeMillis == null) {
            log.warn("Timestamp is null, using current time");
            return LocalDateTime.now();
        }

        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timeMillis),
                ZoneOffset.UTC
        );
    }

    /**
     * Clean USGS ID (remove network prefix like "us6000...")
     * Keep original if cleaning fails
     */
    private String cleanId(String id) {
        if (id == null) {
            return "unknown-" + System.currentTimeMillis();
        }
        return id;
    }
}