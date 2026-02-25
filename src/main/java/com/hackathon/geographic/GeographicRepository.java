package com.hackathon.geographic;

import com.hackathon.response.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GeographicRepository extends JpaRepository<Response, String> {

    // GET /geographic/regional — sorted ascending or descending
    @Query("""
        SELECT new com.example.hackathon.geographic.RegionCountDto(
            r.geoRegion,
            COUNT(r)
        )
        FROM Response r
        GROUP BY r.geoRegion
        ORDER BY COUNT(r) DESC
    """)
    List<RegionCountDto> countByRegionDesc();

    @Query("""
        SELECT new com.example.hackathon.geographic.RegionCountDto(
            r.geoRegion,
            COUNT(r)
        )
        FROM Response r
        GROUP BY r.geoRegion
        ORDER BY COUNT(r) ASC
    """)
    List<RegionCountDto> countByRegionAsc();

    // GET /geographic/channels — partner effectiveness per region
    @Query("""
        SELECT new com.example.hackathon.geographic.RegionChannelDto(
            r.geoRegion,
            rp.accessChannel,
            COUNT(r)
        )
        FROM Response r
        JOIN ResponseProfile rp ON rp.responseId = r.id
        GROUP BY r.geoRegion, rp.accessChannel
        ORDER BY r.geoRegion, COUNT(r) DESC
    """)
    List<RegionChannelDto> channelEffectivenessByRegion();

    // Fetch responses for learner list building per region
    @Query("""
        SELECT r FROM Response r
        JOIN FETCH r.learner
    """)
    List<Response> findAllWithLearner();
}
