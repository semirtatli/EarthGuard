package com.earthguard.earthquake.dto;

import com.earthguard.common.entity.Earthquake;
import org.springframework.stereotype.Component;

@Component
public class EarthquakeMapper {

    public Earthquake toEntity(EarthquakeRequest request) {
        Earthquake earthquake = new Earthquake();
        earthquake.setId(request.getId());
        earthquake.setMagnitude(request.getMagnitude());
        earthquake.setLatitude(request.getLatitude());
        earthquake.setLongitude(request.getLongitude());
        earthquake.setDepth(request.getDepth());
        earthquake.setLocation(request.getLocation());
        earthquake.setTimestamp(request.getTimestamp());
        earthquake.setUrl(request.getUrl());
        // alertLevel otomatik hesaplanacak (@PrePersist)
        return earthquake;
    }

    public EarthquakeResponse toResponse(Earthquake entity) {
        return EarthquakeResponse.builder()
                .id(entity.getId())
                .magnitude(entity.getMagnitude())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .depth(entity.getDepth())
                .location(entity.getLocation())
                .timestamp(entity.getTimestamp())
                .url(entity.getUrl())
                .alertLevel(entity.getAlertLevel())
                .alertDescription(entity.getAlertLevel() != null ?
                        entity.getAlertLevel().getDescription() : null)
                .createdAt(entity.getCreatedAt())
                .build();
    }
}