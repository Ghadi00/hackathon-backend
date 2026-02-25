package com.hackathon.performance;

import com.hackathon.response.ResponseProfile;
import com.hackathon.response.ResponseProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final ResponseProfileRepository profileRepository;

    // GET /performance/registrations
    public List<Map<String, Object>> getRegistrationsByChannel() {
        List<ChannelCountDto> counts = performanceRepository.countByChannel();

        // Fetch all profiles once to build learner lists per channel
        List<ResponseProfile> profiles = profileRepository.findAll();
        Map<String, List<Map<String, String>>> learnersByChannel = profiles.stream()
                .collect(Collectors.groupingBy(
                        ResponseProfile::getAccessChannel,
                        Collectors.mapping(
                                rp -> Map.of(
                                        "id",   rp.getResponseId(),
                                        "name", rp.getResponse().getLearner().getFullName()
                                ),
                                Collectors.toList()
                        )
                ));

        return counts.stream()
                .map(c -> Map.<String, Object>of(
                        "channel",  c.channel(),
                        "count",    c.count(),
                        "learners", learnersByChannel.getOrDefault(c.channel(), List.of())
                ))
                .toList();
    }

    // GET /performance/capabilities
    public List<ChannelEntityDto> getCapabilities() {
        return performanceRepository.trafficByChannelAndEntity();
    }

    // GET /performance/growth
    public List<GrowthDto> getGrowth() {
        return performanceRepository.registrationsOverTime();
    }
}
