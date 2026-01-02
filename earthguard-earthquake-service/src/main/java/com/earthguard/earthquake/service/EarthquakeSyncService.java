package com.earthguard.earthquake.service;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.earthquake.client.UsgsEarthquakeClient;
import com.earthguard.earthquake.client.UsgsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EarthquakeSyncService {

    private final UsgsEarthquakeClient usgsClient;
    private final UsgsMapper usgsMapper;
    private final EarthquakeService earthquakeService;

    /**
     * Fetch and save earthquakes from USGS API
     */
    @Transactional
    public int syncEarthquakes(Double minMagnitude, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Starting earthquake sync from USGS...");

        try {
            var response = usgsClient.fetchEarthquakes(minMagnitude, startTime, endTime);

            if (response == null || response.getFeatures() == null || response.getFeatures().isEmpty()) {
                log.warn("No earthquakes returned from USGS API");
                return 0;
            }

            List<Earthquake> earthquakes = usgsMapper.toEarthquakes(response);

            log.info("Mapped {} valid earthquakes from USGS response", earthquakes.size());

            int savedCount = 0;
            int skippedCount = 0;
            int errorCount = 0;

            for (Earthquake earthquake : earthquakes) {
                try {
                    // Check if already exists
                    if (earthquakeService.findById(earthquake.getId()).isPresent()) {
                        log.debug("Earthquake already exists: {}", earthquake.getId());
                        skippedCount++;
                        continue;
                    }

                    // Validate before saving
                    if (earthquake.getMagnitude() == null ||
                            earthquake.getLatitude() == null ||
                            earthquake.getLongitude() == null) {
                        log.warn("Skipping earthquake {} - missing required fields", earthquake.getId());
                        errorCount++;
                        continue;
                    }

                    earthquakeService.save(earthquake);
                    savedCount++;

                } catch (Exception e) {
                    log.error("Failed to save earthquake: {}", earthquake.getId(), e);
                    errorCount++;
                }
            }

            log.info("Sync completed: {} new saved, {} skipped (duplicates), {} errors",
                    savedCount, skippedCount, errorCount);

            return savedCount;

        } catch (Exception e) {
            log.error("Fatal error during earthquake sync", e);
            throw e;
        }
    }
}