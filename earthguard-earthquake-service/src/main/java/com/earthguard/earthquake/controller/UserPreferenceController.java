package com.earthguard.earthquake.controller;

import com.earthguard.earthquake.dto.preference.UserPreferenceRequest;
import com.earthguard.earthquake.dto.preference.UserPreferenceResponse;
import com.earthguard.earthquake.service.UserPreferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
@Slf4j
public class UserPreferenceController {

    private final UserPreferenceService preferenceService;

    @PutMapping
    public ResponseEntity<UserPreferenceResponse> updatePreferences(
            Authentication authentication,
            @Valid @RequestBody UserPreferenceRequest request) {

        String username = authentication.getName();
        log.info("Update preference request from user: {}", username);

        UserPreferenceResponse response = preferenceService.createOrUpdatePreference(username, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<UserPreferenceResponse> getPreferences(Authentication authentication) {
        String username = authentication.getName();
        log.debug("Get preference request from user: {}", username);

        UserPreferenceResponse response = preferenceService.getPreference(username);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePreferences(Authentication authentication) {
        String username = authentication.getName();
        log.info("Delete preference request from user: {}", username);

        preferenceService.deletePreference(username);

        return ResponseEntity.noContent().build();
    }
}