package com.earthguard.earthquake.service;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.common.enums.AlertLevel;
import com.earthguard.earthquake.dto.filter.EarthquakeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EarthquakeService {

    // CRUD Operations
    Earthquake save(Earthquake earthquake);
    Optional<Earthquake> findById(String id);
    List<Earthquake> findAll();
    void deleteById(String id);

    // Business Queries
    List<Earthquake> findByMagnitude(Double minMagnitude);
    List<Earthquake> findByAlertLevel(AlertLevel alertLevel);
    List<Earthquake> findRecent(int limit);
    List<Earthquake> findByDateRange(LocalDateTime start, LocalDateTime end);
    List<Earthquake> findByLocation(String location);
    List<Earthquake> findNearby(Double latitude, Double longitude);

    Page<Earthquake> findAll(Pageable pageable);
    Page<Earthquake> findWithFilter(EarthquakeFilter filter, Pageable pageable);
    // Statistics
    long getTotalCount();
    List<Earthquake> findRecentCriticalAlerts(int hours);
}