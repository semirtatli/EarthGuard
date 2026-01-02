package com.earthguard.earthquake.client;

import com.earthguard.common.entity.Earthquake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@Slf4j
public class UsgsMapper {

    public Earthquake toEarthquake(UsgsEarthquakeResponse.Feature feature) {
        try {
            Earthquake earthquake = new Earthquake();

            // ID
            earthquake.setId(feature.getId());

            // Properties
            var props = feature.getProperties();
            earthquake.setMagnitude(props.getMag());
            earthquake.setLocation(props.getPlace());
            earthquake.setUrl(props.getUrl());

            // Timestamp (USGS uses milliseconds since epoch)
            if (props.getTime() != null) {
                earthquake.setTimestamp(
                        LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(props.getTime()),
                                ZoneOffset.UTC
                        )
                );
            }

            // Geometry (coordinates: [longitude, latitude, depth])
            var coords = feature.getGeometry().getCoordinates();
            if (coords != null && coords.size() >= 3) {
                earthquake.setLongitude(coords.get(0));
                earthquake.setLatitude(coords.get(1));
                earthquake.setDepth(coords.get(2).intValue());
            }

            return earthquake;

        } catch (Exception e) {
            log.error("Error mapping USGS feature to Earthquake: {}", feature.getId(), e);
            return null;
        }
    }

    public List<Earthquake> toEarthquakes(UsgsEarthquakeResponse response) {
        if (response == null || response.getFeatures() == null) {
            return List.of();
        }

        return response.getFeatures().stream()
                .map(this::toEarthquake)
                .filter(e -> e != null)
                .toList();
    }
}