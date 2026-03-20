package com.earthguard.earthquake.dto.preference;

import com.earthguard.common.entity.UserPreference;
import org.springframework.stereotype.Component;

@Component
public class UserPreferenceMapper {

    public UserPreferenceResponse toResponse(UserPreference preference) {
        return UserPreferenceResponse.builder()
                .id(preference.getId())
                .preferredLatitude(preference.getPreferredLatitude())
                .preferredLongitude(preference.getPreferredLongitude())
                .radiusKm(preference.getRadiusKm())
                .locationName(preference.getLocationName())
                .minMagnitude(preference.getMinMagnitude())
                .emailNotifications(preference.getEmailNotifications())
                .pushNotifications(preference.getPushNotifications())
                .criticalOnly(preference.getCriticalOnly())
                .createdAt(preference.getCreatedAt())
                .updatedAt(preference.getUpdatedAt())
                .build();
    }

    public void updateEntity(UserPreference preference, UserPreferenceRequest request) {
        if (request.getPreferredLatitude() != null) preference.setPreferredLatitude(request.getPreferredLatitude());
        if (request.getPreferredLongitude() != null) preference.setPreferredLongitude(request.getPreferredLongitude());
        if (request.getRadiusKm() != null) preference.setRadiusKm(request.getRadiusKm());
        if (request.getLocationName() != null) preference.setLocationName(request.getLocationName());
        if (request.getMinMagnitude() != null) preference.setMinMagnitude(request.getMinMagnitude());
        if (request.getEmailNotifications() != null) preference.setEmailNotifications(request.getEmailNotifications());
        if (request.getPushNotifications() != null) preference.setPushNotifications(request.getPushNotifications());
        if (request.getCriticalOnly() != null) preference.setCriticalOnly(request.getCriticalOnly());
    }
}
