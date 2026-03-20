package com.earthguard.earthquake.controller;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.common.enums.AlertLevel;
import com.earthguard.earthquake.dto.EarthquakeMapper;
import com.earthguard.earthquake.dto.EarthquakeRequest;
import com.earthguard.earthquake.dto.EarthquakeResponse;
import com.earthguard.earthquake.exception.ResourceNotFoundException;
import com.earthguard.earthquake.service.EarthquakeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.earthguard.earthquake.dto.PageResponse;
import com.earthguard.earthquake.dto.filter.EarthquakeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/earthquakes")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Earthquakes", description = "Earthquake data management and search endpoints")
public class EarthquakeController {

    private final EarthquakeService earthquakeService;
    private final EarthquakeMapper mapper;

    @PostMapping
    @Operation(summary = "Create earthquake", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<EarthquakeResponse> create(@Valid @RequestBody EarthquakeRequest request) {
        log.info("Creating earthquake: {}", request.getId());
        var saved = earthquakeService.save(mapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get earthquake by ID")
    public ResponseEntity<EarthquakeResponse> getById(@PathVariable String id) {
        var earthquake = earthquakeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Earthquake", "id", id));
        return ResponseEntity.ok(mapper.toResponse(earthquake));
    }

    @GetMapping
    @Operation(summary = "Get all earthquakes")
    public ResponseEntity<List<EarthquakeResponse>> getAll() {
        return ResponseEntity.ok(mapper.toResponseList(earthquakeService.findAll()));
    }

    @GetMapping("/magnitude/{minMagnitude}")
    @Operation(summary = "Get earthquakes by minimum magnitude")
    public ResponseEntity<List<EarthquakeResponse>> getByMagnitude(@PathVariable Double minMagnitude) {
        return ResponseEntity.ok(mapper.toResponseList(earthquakeService.findByMagnitude(minMagnitude)));
    }

    @GetMapping("/alert-level/{level}")
    @Operation(summary = "Get earthquakes by alert level")
    public ResponseEntity<List<EarthquakeResponse>> getByAlertLevel(@PathVariable AlertLevel level) {
        return ResponseEntity.ok(mapper.toResponseList(earthquakeService.findByAlertLevel(level)));
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent earthquakes", description = "Returns the most recent earthquakes (public endpoint)")
    public ResponseEntity<List<EarthquakeResponse>> getRecent(
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit) {
        return ResponseEntity.ok(mapper.toResponseList(earthquakeService.findRecent(limit)));
    }

    @GetMapping("/location")
    @Operation(summary = "Search earthquakes by location text")
    public ResponseEntity<List<EarthquakeResponse>> getByLocation(@RequestParam String location) {
        return ResponseEntity.ok(mapper.toResponseList(earthquakeService.findByLocation(location)));
    }

    @GetMapping("/nearby")
    @Operation(summary = "Get earthquakes near coordinates")
    public ResponseEntity<List<EarthquakeResponse>> getNearby(
            @RequestParam Double latitude, @RequestParam Double longitude) {
        return ResponseEntity.ok(mapper.toResponseList(earthquakeService.findNearby(latitude, longitude)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete earthquake", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("Deleting earthquake: {}", id);
        earthquakeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Get total earthquake count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(earthquakeService.getTotalCount());
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get earthquakes with pagination")
    public ResponseEntity<PageResponse<EarthquakeResponse>> getAllPaginated(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(buildPageResponse(earthquakeService.findAll(pageable)));
    }

    @GetMapping("/search")
    @Operation(summary = "Advanced search with filters and pagination")
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
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        EarthquakeFilter filter = EarthquakeFilter.builder()
                .minMagnitude(minMagnitude).maxMagnitude(maxMagnitude)
                .startDate(startDate).endDate(endDate)
                .location(location)
                .latitude(latitude).longitude(longitude).radiusKm(radiusKm)
                .alertLevel(alertLevel)
                .minDepth(minDepth).maxDepth(maxDepth)
                .build();

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(buildPageResponse(earthquakeService.findWithFilter(filter, pageable)));
    }

    private PageResponse<EarthquakeResponse> buildPageResponse(Page<Earthquake> page) {
        return PageResponse.<EarthquakeResponse>builder()
                .content(mapper.toResponseList(page.getContent()))
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
