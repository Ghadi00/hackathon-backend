package com.hackathon.geographic;

import com.example.hackathon.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeographicService {

    private final GeographicRepository geographicRepository;

    // GET /geographic/regional
    public List<Map<String, Object>> getRegionalDistribution(String sort) {
        List<RegionCountDto> counts = "asc".equalsIgnoreCase(sort)
                ? geographicRepository.countByRegionAsc()
                : geographicRepository.countByRegionDesc();

        List<Response> responses = geographicRepository.findAllWithLearner();
        Map<String, List<Map<String, String>>> learnersByRegion = responses.stream()
                .collect(Collectors.groupingBy(
                        Response::getGeoRegion,
                        Collectors.mapping(
                                r -> Map.of("id", r.getId(), "name", r.getLearner().getFullName()),
                                Collectors.toList()
                        )
                ));

        return counts.stream()
                .map(c -> Map.<String, Object>of(
                        "name",     c.region(),
                        "count",    c.total(),
                        "learners", learnersByRegion.getOrDefault(c.region(), List.of())
                ))
                .toList();
    }

    // GET /geographic/channels
    public List<RegionChannelDto> getChannelByRegion() {
        return geographicRepository.channelEffectivenessByRegion();
    }
}
