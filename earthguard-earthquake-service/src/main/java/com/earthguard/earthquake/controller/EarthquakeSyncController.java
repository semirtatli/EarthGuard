package com.earthguard.earthquake.controller;

import com.earthguard.earthquake.service.EarthquakeSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/earthquakes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "USGS Sync", description = "USGS earthquake data synchronization (Admin only)")
public class EarthquakeSyncController {

    private final EarthquakeSyncService syncService;

    @PostMapping("/sync")
    @Operation(
            summary = "Sync earthquakes from USGS",
            description = "Manually trigger earthquake data synchronization from USGS API",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<String> syncFromUsgs(
            @RequestParam(defaultValue = "4.5") Double minMagnitude,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        log.info("Manual USGS sync triggered: minMagnitude={}", minMagnitude);

        LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime) : null;
        LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime) : null;

        int count = syncService.syncEarthquakes(minMagnitude, start, end);

        return ResponseEntity.ok(String.format("Successfully synced %d new earthquakes from USGS", count));
    }
}
