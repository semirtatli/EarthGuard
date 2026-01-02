package com.earthguard.earthquake.scheduler;

import com.earthguard.earthquake.service.EarthquakeSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class EarthquakeScheduler {

    private final EarthquakeSyncService syncService;

    /**
     * Sync earthquakes every 5 minutes
     * Fetches earthquakes with magnitude >= 4.5 from last 1 hour
     */
    @Scheduled(fixedRate = 300000) // 5 minutes = 300,000 milliseconds
    public void syncEarthquakesFromUsgs() {
        log.info("=== SCHEDULED TASK STARTED: Syncing earthquakes from USGS ===");

        try {
            // Fetch earthquakes from last 1 hour with magnitude >= 4.5
            LocalDateTime startTime = LocalDateTime.now().minusHours(1);
            LocalDateTime endTime = LocalDateTime.now();

            int count = syncService.syncEarthquakes(4.5, startTime, endTime);

            log.info("=== SCHEDULED TASK COMPLETED: {} new earthquakes synced ===", count);

        } catch (Exception e) {
            log.error("=== SCHEDULED TASK FAILED ===", e);
        }
    }

    /**
     * Initial sync on application startup
     * Fetches last 7 days of earthquakes
     */
    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE) // Run once after 10 seconds
    public void initialSync() {
        log.info("=== INITIAL SYNC STARTED: Loading historical data ===");

        try {
            LocalDateTime startTime = LocalDateTime.now().minusDays(7);
            LocalDateTime endTime = LocalDateTime.now();

            int count = syncService.syncEarthquakes(4.5, startTime, endTime);

            log.info("=== INITIAL SYNC COMPLETED: {} earthquakes loaded ===", count);

        } catch (Exception e) {
            log.error("=== INITIAL SYNC FAILED ===", e);
        }
    }
}
