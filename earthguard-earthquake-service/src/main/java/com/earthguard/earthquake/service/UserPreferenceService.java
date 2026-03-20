package com.earthguard.earthquake.service;

import com.earthguard.common.entity.User;
import com.earthguard.common.entity.UserPreference;
import com.earthguard.earthquake.dto.preference.UserPreferenceMapper;
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
    private final UserPreferenceMapper mapper;

    @Transactional
    public UserPreferenceResponse createOrUpdatePreference(String username, UserPreferenceRequest request) {
        log.info("Updating preferences for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        UserPreference preference = preferenceRepository.findByUserId(user.getId())
                .orElse(UserPreference.builder().user(user).build());

        mapper.updateEntity(preference, request);

        UserPreference saved = preferenceRepository.save(preference);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserPreferenceResponse getPreference(String username) {
        UserPreference preference = preferenceRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("UserPreference", "username", username));
        return mapper.toResponse(preference);
    }

    @Transactional
    public void deletePreference(String username) {
        UserPreference preference = preferenceRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("UserPreference", "username", username));
        preferenceRepository.delete(preference);
        log.info("Deleted preferences for user: {}", username);
    }
}
