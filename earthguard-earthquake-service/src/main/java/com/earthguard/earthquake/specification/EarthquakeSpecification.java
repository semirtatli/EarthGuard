package com.earthguard.earthquake.specification;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.earthquake.dto.filter.EarthquakeFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeSpecification {

    private EarthquakeSpecification() {}

    public static Specification<Earthquake> withFilter(EarthquakeFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getMinMagnitude() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("magnitude"), filter.getMinMagnitude()));
            }
            if (filter.getMaxMagnitude() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("magnitude"), filter.getMaxMagnitude()));
            }

            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), filter.getStartDate()));
            }
            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), filter.getEndDate()));
            }

            if (filter.getLocation() != null && !filter.getLocation().isBlank()) {
                String escaped = escapeLikePattern(filter.getLocation().toLowerCase());
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + escaped + "%"));
            }

            if (filter.getLatitude() != null && filter.getLongitude() != null && filter.getRadiusKm() != null) {
                double latDelta = filter.getRadiusKm() / 111.0;
                double lonDelta = filter.getRadiusKm() / (111.0 * Math.cos(Math.toRadians(filter.getLatitude())));

                predicates.add(cb.between(root.get("latitude"),
                        filter.getLatitude() - latDelta, filter.getLatitude() + latDelta));
                predicates.add(cb.between(root.get("longitude"),
                        filter.getLongitude() - lonDelta, filter.getLongitude() + lonDelta));
            }

            if (filter.getAlertLevel() != null) {
                predicates.add(cb.equal(root.get("alertLevel"), filter.getAlertLevel()));
            }

            if (filter.getMinDepth() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("depth"), filter.getMinDepth()));
            }
            if (filter.getMaxDepth() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("depth"), filter.getMaxDepth()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String escapeLikePattern(String input) {
        return input
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
