package com.earthguard.earthquake.repository;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.common.enums.AlertLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EarthquakeRepository extends JpaRepository<Earthquake, String>, JpaSpecificationExecutor<Earthquake> {

    // Filter by Magnitude
    List<Earthquake> findByMagnitudeGreaterThanEqual(Double magnitude);

    // Filter by Alert Level
    List<Earthquake> findByAlertLevel(AlertLevel alertLevel);

    // Bring the data between specified times
    List<Earthquake> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    // Last N earthquake
    List<Earthquake> findTop10ByOrderByTimestampDesc();

    // Location based search (basic version - text search)
    List<Earthquake> findByLocationContainingIgnoreCase(String location);

    // Coordinate based search (approximately with 50km radius)
    @Query("SELECT e FROM Earthquake e WHERE " +
            "ABS(e.latitude - :lat) <= 0.5 AND " +
            "ABS(e.longitude - :lon) <= 0.5")
    List<Earthquake> findNearby(@Param("lat") Double latitude,
                                @Param("lon") Double longitude);

    // Magnitude and tarih combination
    @Query("SELECT e FROM Earthquake e WHERE " +
            "e.magnitude >= :minMag AND " +
            "e.timestamp >= :since " +
            "ORDER BY e.timestamp DESC")
    List<Earthquake> findRecentStrongEarthquakes(@Param("minMag") Double minMagnitude,
                                                 @Param("since") LocalDateTime since);
}
