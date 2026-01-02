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

        var response = usgsClient.fetchEarthquakes(minMagnitude, startTime, endTime);
        List<Earthquake> earthquakes = usgsMapper.toEarthquakes(response);

        log.info("Mapped {} earthquakes from USGS response", earthquakes.size());

        int savedCount = 0;
        int skippedCount = 0;

        for (Earthquake earthquake : earthquakes) {
            try {
                // Check if already exists
                if (earthquakeService.findById(earthquake.getId()).isPresent()) {
                    log.debug("Earthquake already exists: {}", earthquake.getId());
                    skippedCount++;
                    continue;
                }

                earthquakeService.save(earthquake);
                savedCount++;

            } catch (Exception e) {
                log.error("Failed to save earthquake: {}", earthquake.getId(), e);
            }
        }

        log.info("Sync completed: {} new earthquakes saved, {} skipped", savedCount, skippedCount);
        return savedCount;
    }
}