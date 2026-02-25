package com.hackathon.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // GET /profiles/admins
    @GetMapping("/admins")
    public ResponseEntity<Map<String, Object>> getAdminProfiles() {
        return ResponseEntity.ok(Map.of("profiles", profileService.getAdminProfiles()));
    }

    // GET /profiles/demographics
    @GetMapping("/demographics")
    public ResponseEntity<Map<String, Object>> getDemographics() {
        return ResponseEntity.ok(Map.of("profiles", profileService.getDemographics()));
    }

    // GET /profiles/programs
    @GetMapping("/programs")
    public ResponseEntity<Map<String, Object>> getProgramProfiles() {
        return ResponseEntity.ok(Map.of("profiles", profileService.getProgramProfiles()));
    }
}