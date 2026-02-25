package com.hackathon.survey;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyIngestionService ingestionService;

    // POST /survey/ingest — manually trigger a sync
    @PostMapping("/ingest")
    public ResponseEntity<Map<String, String>> triggerIngestion() {
        ingestionService.ingest();
        return ResponseEntity.ok(Map.of("status", "ingestion triggered"));
    }
}
