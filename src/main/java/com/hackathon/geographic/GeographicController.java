package com.hackathon.geographic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/geographic")
@RequiredArgsConstructor
public class GeographicController {

    private final GeographicService geographicService;

    // GET /geographic/regional?sort=asc|desc
    @GetMapping("/regional")
    public ResponseEntity<Map<String, Object>> getRegional(
            @RequestParam(defaultValue = "desc") String sort) {
        return ResponseEntity.ok(Map.of("regions", geographicService.getRegionalDistribution(sort)));
    }

    // GET /geographic/channels
    @GetMapping("/channels")
    public ResponseEntity<Map<String, Object>> getChannels() {
        return ResponseEntity.ok(Map.of("regions", geographicService.getChannelByRegion()));
    }
}