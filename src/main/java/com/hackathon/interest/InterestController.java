package com.hackathon.interest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    // GET /interests/areas
    @GetMapping("/areas")
    public ResponseEntity<Map<String, Object>> getAreas() {
        List<GoalDemandDto> areas = interestService.getAiGoalDemand();
        return ResponseEntity.ok(Map.of("areas", areas));
    }

    // GET /interests/motivations
    @GetMapping("/motivations")
    public ResponseEntity<Map<String, Object>> getMotivations() {
        return ResponseEntity.ok(Map.of("motivations", interestService.getMotivations()));
    }
}
