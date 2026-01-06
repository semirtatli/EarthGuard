package com.earthguard.earthquake.controller;

import com.earthguard.earthquake.dto.preference.UserPreferenceRequest;
import com.earthguard.earthquake.dto.preference.UserPreferenceResponse;
import com.earthguard.earthquake.service.UserPreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Preferences", description = "User location and notification preferences")
@SecurityRequirement(name = "bearerAuth")
public class UserPreferenceController {

    private final UserPreferenceService preferenceService;

    @PutMapping
    @Operation(
            summary = "Update user preferences",
            description = "Create or update location-based filtering and notification preferences"
    )
    public ResponseEntity<UserPreferenceResponse> updatePreferences(
            Authentication authentication,
            @Valid @RequestBody UserPreferenceRequest request) {

        String username = authentication.getName();
        log.info("Update preference request from user: {}", username);

        UserPreferenceResponse response = preferenceService.createOrUpdatePreference(username, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Get user preferences",
            description = "Retrieve current user's preferences"
    )
    public ResponseEntity<UserPreferenceResponse> getPreferences(Authentication authentication) {
        String username = authentication.getName();
        log.debug("Get preference request from user: {}", username);

        UserPreferenceResponse response = preferenceService.getPreference(username);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(
            summary = "Delete user preferences",
            description = "Remove all preferences and reset to defaults"
    )
    public ResponseEntity<Void> deletePreferences(Authentication authentication) {
        String username = authentication.getName();
        log.info("Delete preference request from user: {}", username);

        preferenceService.deletePreference(username);

        return ResponseEntity.noContent().build();
    }
}