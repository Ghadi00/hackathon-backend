package com.hackathon.performance;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/performance")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    // GET /performance/registrations
    @GetMapping("/registrations")
    public ResponseEntity<Map<String, Object>> getRegistrations() {
        return ResponseEntity.ok(Map.of("channels", performanceService.getRegistrationsByChannel()));
    }

    // GET /performance/capabilities
    @GetMapping("/capabilities")
    public ResponseEntity<Map<String, Object>> getCapabilities() {
        return ResponseEntity.ok(Map.of("channels", performanceService.getCapabilities()));
    }

    // GET /performance/growth
    @GetMapping("/growth")
    public ResponseEntity<Map<String, Object>> getGrowth() {
        return ResponseEntity.ok(Map.of("registrations", performanceService.getGrowth()));
    }
}
