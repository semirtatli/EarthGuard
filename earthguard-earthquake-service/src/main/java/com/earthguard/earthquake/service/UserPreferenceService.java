package com.earthguard.earthquake.service;

import com.earthguard.common.entity.User;
import com.earthguard.common.entity.UserPreference;
import com.earthguard.earthquake.dto.preference.UserPreferenceRequest;
import com.earthguard.earthquake.dto.preference.UserPreferenceResponse;
import com.earthguard.earthquake.exception.ResourceNotFoundException;
import com.earthguard.earthquake.repository.UserPreferenceRepository;
import com.earthguard.earthquake.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPreferenceService {

    private final UserPreferenceRepository preferenceRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserPreferenceResponse createOrUpdatePreference(String username, UserPreferenceRequest request) {
        log.info("Creating/updating preference for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        UserPreference preference = preferenceRepository.findByUserId(user.getId())
                .orElse(UserPreference.builder()
                        .user(user)
                        .build());

        // Update fields
        if (request.getPreferredLatitude() != null) {
            preference.setPreferredLatitude(request.getPreferredLatitude());
        }
        if (request.getPreferredLongitude() != null) {
            preference.setPreferredLongitude(request.getPreferredLongitude());
        }
        if (request.getRadiusKm() != null) {
            preference.setRadiusKm(request.getRadiusKm());
        }
        if (request.getLocationName() != null) {
            preference.setLocationName(request.getLocationName());
        }
        if (request.getMinMagnitude() != null) {
            preference.setMinMagnitude(request.getMinMagnitude());
        }
        if (request.getEmailNotifications() != null) {
            preference.setEmailNotifications(request.getEmailNotifications());
        }
        if (request.getPushNotifications() != null) {
            preference.setPushNotifications(request.getPushNotifications());
        }
        if (request.getCriticalOnly() != null) {
            preference.setCriticalOnly(request.getCriticalOnly());
        }

        UserPreference saved = preferenceRepository.save(preference);

        log.info("Preference saved for user: {}", username);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserPreferenceResponse getPreference(String username) {
        log.debug("Fetching preference for user: {}", username);

        UserPreference preference = preferenceRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User preference not found for user: " + username));

        return toResponse(preference);
    }

    @Transactional
    public void deletePreference(String username) {
        log.info("Deleting preference for user: {}", username);

        UserPreference preference = preferenceRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User preference not found for user: " + username));

        preferenceRepository.delete(preference);
    }

    private UserPreferenceResponse toResponse(UserPreference preference) {
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
}