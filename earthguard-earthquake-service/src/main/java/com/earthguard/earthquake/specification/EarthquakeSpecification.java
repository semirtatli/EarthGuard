package com.earthguard.earthquake.specification;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.earthquake.dto.filter.EarthquakeFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeSpecification {

    public static Specification<Earthquake> withFilter(EarthquakeFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Magnitude range
            if (filter.getMinMagnitude() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("magnitude"), filter.getMinMagnitude()
                ));
            }
            if (filter.getMaxMagnitude() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("magnitude"), filter.getMaxMagnitude()
                ));
            }

            // Date range
            if (filter.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("timestamp"), filter.getStartDate()
                ));
            }
            if (filter.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("timestamp"), filter.getEndDate()
                ));
            }

            // Location text search
            if (filter.getLocation() != null && !filter.getLocation().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")),
                        "%" + filter.getLocation().toLowerCase() + "%"
                ));
            }

            // Radius-based search (approximate)
            if (filter.getLatitude() != null && filter.getLongitude() != null && filter.getRadiusKm() != null) {
                double latDelta = filter.getRadiusKm() / 111.0; // 1 degree ≈ 111 km
                double lonDelta = filter.getRadiusKm() / (111.0 * Math.cos(Math.toRadians(filter.getLatitude())));

                predicates.add(criteriaBuilder.between(
                        root.get("latitude"),
                        filter.getLatitude() - latDelta,
                        filter.getLatitude() + latDelta
                ));
                predicates.add(criteriaBuilder.between(
                        root.get("longitude"),
                        filter.getLongitude() - lonDelta,
                        filter.getLongitude() + lonDelta
                ));
            }

            // Alert level
            if (filter.getAlertLevel() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("alertLevel"), filter.getAlertLevel()
                ));
            }

            // Depth range
            if (filter.getMinDepth() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("depth"), filter.getMinDepth()
                ));
            }
            if (filter.getMaxDepth() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("depth"), filter.getMaxDepth()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}