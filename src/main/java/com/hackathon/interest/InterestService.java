package com.hackathon.interest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final ResponseAiGoalRepository aiGoalRepository;
    private final ResponseLearningReasonRepository learningReasonRepository;

    public List<GoalDemandDto> getAiGoalDemand() {
        return aiGoalRepository.findGoalDemand();
    }

    // Groups raw motivation rows by reason, collecting learner list per reason
    public List<Map<String, Object>> getMotivations() {
        List<MotivationDto> raw = learningReasonRepository.findMotivations();

        return raw.stream()
                .collect(Collectors.groupingBy(MotivationDto::reason))
                .entrySet().stream()
                .map(e -> {
                    List<Map<String, String>> learners = e.getValue().stream()
                            .map(m -> Map.of("id", m.responseId(), "name", m.name()))
                            .toList();
                    return Map.<String, Object>of(
                            "type",     e.getKey(),
                            "count",    learners.size(),
                            "learners", learners
                    );
                })
                .toList();
    }
}
