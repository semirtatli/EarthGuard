package com.earthguard.earthquake.service;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.common.enums.AlertLevel;
import com.earthguard.earthquake.exception.ResourceNotFoundException;
import com.earthguard.earthquake.repository.EarthquakeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.earthguard.earthquake.messaging.EarthquakeEventProducer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.earthguard.earthquake.dto.filter.EarthquakeFilter;
import com.earthguard.earthquake.specification.EarthquakeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EarthquakeServiceImpl implements EarthquakeService {

    private final EarthquakeRepository repository;

    private final EarthquakeEventProducer eventProducer;

    @Override
    @Cacheable(value = "earthquakeById", key = "#id")
    @Transactional(readOnly = true)
    public Optional<Earthquake> findById(String id) {
        log.debug("Finding earthquake by id: {} (cache miss)", id);
        return repository.findById(id);
    }

    @Override
    @Cacheable(value = "earthquakes")
    @Transactional(readOnly = true)
    public List<Earthquake> findAll() {
        log.debug("Finding all earthquakes (cache miss)");
        return repository.findAll();
    }

    @Override
    @CachePut(value = "earthquakeById", key = "#result.id")
    @CacheEvict(value = {"earthquakes", "recentEarthquakes"}, allEntries = true)
    public Earthquake save(Earthquake earthquake) {
        log.info("Saving earthquake: id={}, magnitude={}",
                earthquake.getId(), earthquake.getMagnitude());

        Earthquake saved = repository.save(earthquake);

        log.debug("Earthquake saved successfully with alert level: {}",
                saved.getAlertLevel());

        // Publish event to Kafka
        eventProducer.sendEarthquakeEvent(saved, "CREATED");

        return saved;
    }

    @Override
    @CacheEvict(value = {"earthquakes", "recentEarthquakes", "earthquakeById"}, allEntries = true)
    public void deleteById(String id) {
        log.info("Deleting earthquake: id={}", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Earthquake", "id", id);
        }

        repository.deleteById(id);
    }

    @Override
    @Cacheable(value = "recentEarthquakes", key = "#limit")
    @Transactional(readOnly = true)
    public List<Earthquake> findRecent(int limit) {
        log.debug("Finding {} most recent earthquakes (cache miss)", limit);
        return repository.findTop10ByOrderByTimestampDesc();
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

    @Override
    @Transactional(readOnly = true)
    public Page<Earthquake> findAll(Pageable pageable) {
        log.debug("Finding earthquakes with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Earthquake> findWithFilter(EarthquakeFilter filter, Pageable pageable) {
        log.debug("Finding earthquakes with filter and pagination: {}", filter);
        return repository.findAll(EarthquakeSpecification.withFilter(filter), pageable);
    }
}