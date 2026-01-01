package com.earthguard.earthquake.controller;

import com.earthguard.common.enums.AlertLevel;
import com.earthguard.earthquake.dto.EarthquakeMapper;
import com.earthguard.earthquake.dto.EarthquakeRequest;
import com.earthguard.earthquake.dto.EarthquakeResponse;
import com.earthguard.earthquake.service.EarthquakeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/earthquakes")
@RequiredArgsConstructor
@Slf4j
public class EarthquakeController {

    private final EarthquakeService earthquakeService;
    private final EarthquakeMapper mapper;

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
        return earthquakeService.findById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
}