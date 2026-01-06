package com.earthguard.earthquake.controller;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.common.enums.AlertLevel;
import com.earthguard.earthquake.dto.EarthquakeMapper;
import com.earthguard.earthquake.dto.EarthquakeRequest;
import com.earthguard.earthquake.dto.EarthquakeResponse;
import com.earthguard.earthquake.exception.ResourceNotFoundException;
import com.earthguard.earthquake.service.EarthquakeService;
import com.earthguard.earthquake.service.EarthquakeSyncService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.earthguard.earthquake.dto.PageResponse;
import com.earthguard.earthquake.dto.filter.EarthquakeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/earthquakes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Earthquakes", description = "Earthquake data management and search endpoints")
public class EarthquakeController {

    private final EarthquakeService earthquakeService;
    private final EarthquakeMapper mapper;
    private final EarthquakeSyncService syncService;

    @PostMapping
    public ResponseEntity<EarthquakeResponse> create(@Valid @RequestBody EarthquakeRequest request) {
        log.info("Received request to create earthquake: {}", request.getId());
        var earthquake = mapper.toEntity(request);
        var saved = earthquakeService.save(earthquake);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EarthquakeResponse> getById(@PathVariable String id) {
        log.debug("Fetching earthquake with id: {}", id);
        var earthquake = earthquakeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Earthquake", "id", id));
        return ResponseEntity.ok(mapper.toResponse(earthquake));
    }

    @GetMapping
    public ResponseEntity<List<EarthquakeResponse>> getAll() {
        log.debug("Fetching all earthquakes");
        var earthquakes = earthquakeService.findAll();
        var responses = earthquakes.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/magnitude/{minMagnitude}")
    public ResponseEntity<List<EarthquakeResponse>> getByMagnitude(
            @PathVariable Double minMagnitude) {
        log.debug("Fetching earthquakes with magnitude >= {}", minMagnitude);
        var earthquakes = earthquakeService.findByMagnitude(minMagnitude);
        var responses = earthquakes.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/alert-level/{level}")
    public ResponseEntity<List<EarthquakeResponse>> getByAlertLevel(
            @PathVariable AlertLevel level) {
        log.debug("Fetching earthquakes with alert level: {}", level);
        var earthquakes = earthquakeService.findByAlertLevel(level);
        var responses = earthquakes.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/recent")
    @Operation(
            summary = "Get recent earthquakes",
            description = "Returns the most recent earthquakes (public endpoint)"
    )
    public ResponseEntity<List<EarthquakeResponse>> getRecent(
            @RequestParam(defaultValue = "10") int limit) {
        log.debug("Fetching {} recent earthquakes", limit);
        var earthquakes = earthquakeService.findRecent(limit);
        var responses = earthquakes.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/location")
    public ResponseEntity<List<EarthquakeResponse>> getByLocation(
            @RequestParam String location) {
        log.debug("Searching earthquakes by location: {}", location);
        var earthquakes = earthquakeService.findByLocation(location);
        var responses = earthquakes.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<EarthquakeResponse>> getNearby(
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        log.debug("Searching earthquakes near: {}, {}", latitude, longitude);
        var earthquakes = earthquakeService.findNearby(latitude, longitude);
        var responses = earthquakes.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("Deleting earthquake: {}", id);
        earthquakeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(earthquakeService.getTotalCount());
    }


    @PostMapping("/sync")
    @Operation(
            summary = "Sync earthquakes from USGS",
            description = "Manually trigger earthquake data synchronization from USGS API (Admin only)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<String> syncFromUsgs(
            @RequestParam(required = false, defaultValue = "4.5") Double minMagnitude,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        log.info("Triggering USGS sync: minMagnitude={}", minMagnitude);

        LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime) : null;
        LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime) : null;

        int count = syncService.syncEarthquakes(minMagnitude, start, end);

        return ResponseEntity.ok(
                String.format("Successfully synced %d new earthquakes from USGS", count)
        );
    }

    /**
     * Get earthquakes with pagination and sorting
     *
     * @param page Page number (0-based)
     * @param size Page size
     * @param sortBy Sort field (e.g., magnitude, timestamp)
     * @param direction Sort direction (ASC or DESC)
     */
    @GetMapping("/paginated")
    @Operation(
            summary = "Get earthquakes with pagination",
            description = "Returns paginated earthquake data with sorting options",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<PageResponse<EarthquakeResponse>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        log.debug("Fetching earthquakes: page={}, size={}, sortBy={}, direction={}",
                page, size, sortBy, direction);

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Earthquake> earthquakePage = earthquakeService.findAll(pageable);

        PageResponse<EarthquakeResponse> response = buildPageResponse(earthquakePage);

        return ResponseEntity.ok(response);
    }

    /**
     * Advanced search with filters, pagination, and sorting
     */
    @GetMapping("/search")
    public ResponseEntity<PageResponse<EarthquakeResponse>> search(
            @RequestParam(required = false) Double minMagnitude,
            @RequestParam(required = false) Double maxMagnitude,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double radiusKm,
            @RequestParam(required = false) AlertLevel alertLevel,
            @RequestParam(required = false) Integer minDepth,
            @RequestParam(required = false) Integer maxDepth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        log.info("Advanced search request: minMag={}, maxMag={}, location={}, page={}, size={}",
                minMagnitude, maxMagnitude, location, page, size);

        EarthquakeFilter filter = EarthquakeFilter.builder()
                .minMagnitude(minMagnitude)
                .maxMagnitude(maxMagnitude)
                .startDate(startDate)
                .endDate(endDate)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .radiusKm(radiusKm)
                .alertLevel(alertLevel)
                .minDepth(minDepth)
                .maxDepth(maxDepth)
                .build();

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Earthquake> earthquakePage = earthquakeService.findWithFilter(filter, pageable);

        PageResponse<EarthquakeResponse> response = buildPageResponse(earthquakePage);

        log.info("Search completed: {} results found", response.getTotalElements());

        return ResponseEntity.ok(response);
    }

    /**
     * Helper method to convert Page to PageResponse
     */
    private PageResponse<EarthquakeResponse> buildPageResponse(Page<Earthquake> page) {
        List<EarthquakeResponse> content = page.getContent().stream()
                .map(mapper::toResponse)
                .toList();

        return PageResponse.<EarthquakeResponse>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }
}