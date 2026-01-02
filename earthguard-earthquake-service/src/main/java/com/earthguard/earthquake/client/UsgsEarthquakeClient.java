package com.earthguard.earthquake.client;

import com.earthguard.earthquake.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsgsEarthquakeClient {

    private static final String USGS_BASE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private final RestTemplate restTemplate;

    /**
     * Fetch earthquakes from USGS API
     *
     * @param minMagnitude minimum magnitude (e.g., 4.5)
     * @param startTime start time (can be null for last 30 days)
     * @param endTime end time (can be null for now)
     * @return USGS API response
     */
    public UsgsEarthquakeResponse fetchEarthquakes(
            Double minMagnitude,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        log.info("Fetching earthquakes from USGS API: minMag={}, start={}, end={}",
                minMagnitude, startTime, endTime);

        try {
            String url = buildUrl(minMagnitude, startTime, endTime);
            log.debug("USGS API URL: {}", url);

            UsgsEarthquakeResponse response = restTemplate.getForObject(url, UsgsEarthquakeResponse.class);

            if (response != null && response.getFeatures() != null) {
                log.info("Successfully fetched {} earthquakes from USGS",
                        response.getFeatures().size());
            }

            return response;

        } catch (Exception e) {
            log.error("Error fetching data from USGS API", e);
            throw new InvalidRequestException("Failed to fetch earthquake data from USGS: " + e.getMessage());
        }
    }

    private String buildUrl(Double minMagnitude, LocalDateTime startTime, LocalDateTime endTime) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(USGS_BASE_URL)
                .queryParam("format", "geojson")
                .queryParam("orderby", "time");

        if (minMagnitude != null) {
            builder.queryParam("minmagnitude", minMagnitude);
        }

        if (startTime != null) {
            builder.queryParam("starttime", formatUsgsTimestamp(startTime));  // ← DEĞİŞTİ
        } else {
            // Default: last 30 days
            builder.queryParam("starttime", formatUsgsTimestamp(LocalDateTime.now().minusDays(30)));  // ← DEĞİŞTİ
        }

        if (endTime != null) {
            builder.queryParam("endtime", formatUsgsTimestamp(endTime));  // ← DEĞİŞTİ
        } else {
            builder.queryParam("endtime", formatUsgsTimestamp(LocalDateTime.now()));  // ← DEĞİŞTİ
        }

        return builder.toUriString();
    }

    /**
     * Format timestamp for USGS API (no nanoseconds)
     * USGS accepts: 2026-01-02T03:37:22Z
     * NOT: 2026-01-02T03:37:22.812253400Z
     */
    private String formatUsgsTimestamp(LocalDateTime dateTime) {
        return dateTime
                .atZone(ZoneOffset.UTC)
                .withNano(0)  // Remove nanoseconds
                .toInstant()
                .toString();
    }
}