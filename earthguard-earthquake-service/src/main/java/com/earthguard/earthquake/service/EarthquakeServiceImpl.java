package com.earthguard.earthquake.service;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.common.enums.AlertLevel;
import com.earthguard.earthquake.exception.ResourceNotFoundException;
import com.earthguard.earthquake.repository.EarthquakeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EarthquakeServiceImpl implements EarthquakeService {

    private final EarthquakeRepository repository;

    @Override
    public Earthquake save(Earthquake earthquake) {
        log.info("Saving earthquake: id={}, magnitude={}",
                earthquake.getId(), earthquake.getMagnitude());

        // Calculate Alert level automatically (with @PrePersist)
        Earthquake saved = repository.save(earthquake);

        log.debug("Earthquake saved successfully with alert level: {}",
                saved.getAlertLevel());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Earthquake> findById(String id) {
        log.debug("Finding earthquake by id: {}", id);
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Earthquake> findAll() {
        log.debug("Finding all earthquakes");
        return repository.findAll();
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting earthquake: id={}", id);

        // First check if it exists
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Earthquake", "id", id);
        }

        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Earthquake> findByMagnitude(Double minMagnitude) {
        log.debug("Finding earthquakes with magnitude >= {}", minMagnitude);
        return repository.findByMagnitudeGreaterThanEqual(minMagnitude);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Earthquake> findByAlertLevel(AlertLevel alertLevel) {
        log.debug("Finding earthquakes with alert level: {}", alertLevel);
        return repository.findByAlertLevel(alertLevel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Earthquake> findRecent(int limit) {
        log.debug("Finding {} most recent earthquakes", limit);
        // Note: There is no limit parameter inside JpaRepository , that is why top 10 is used
        return repository.findTop10ByOrderByTimestampDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Earthquake> findByDateRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Finding earthquakes between {} and {}", start, end);
        return repository.findByTimestampBetween(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Earthquake> findByLocation(String location) {
        log.debug("Finding earthquakes near location: {}", location);
        return repository.findByLocationContainingIgnoreCase(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Earthquake> findNearby(Double latitude, Double longitude) {
        log.debug("Finding earthquakes near coordinates: {}, {}", latitude, longitude);
        return repository.findNearby(latitude, longitude);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalCount() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Earthquake> findRecentCriticalAlerts(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        log.info("Finding critical earthquakes in last {} hours (since: {})", hours, since);
        return repository.findRecentStrongEarthquakes(6.0, since);
    }
}