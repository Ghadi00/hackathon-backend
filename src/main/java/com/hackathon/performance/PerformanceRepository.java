package com.hackathon.performance;

import com.hackathon.response.ResponseProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PerformanceRepository extends JpaRepository<ResponseProfile, String> {

    // GET /performance/registrations — count per channel
    @Query("""
        SELECT new com.example.hackathon.performance.ChannelCountDto(
            rp.accessChannel,
            COUNT(rp)
        )
        FROM ResponseProfile rp
        GROUP BY rp.accessChannel
    """)
    List<ChannelCountDto> countByChannel();

    // GET /performance/capabilities — traffic per channel + entity
    @Query("""
        SELECT new com.example.hackathon.performance.ChannelEntityDto(
            rp.accessChannel,
            rp.accessEntityName,
            COUNT(rp)
        )
        FROM ResponseProfile rp
        GROUP BY rp.accessChannel, rp.accessEntityName
        ORDER BY COUNT(rp) DESC
    """)
    List<ChannelEntityDto> trafficByChannelAndEntity();

    // GET /performance/growth — registrations per day
    @Query("""
        SELECT new com.example.hackathon.performance.GrowthDto(
            CAST(r.submittedAt AS localdate),
            COUNT(r)
        )
        FROM com.example.hackathon.response.Response r
        GROUP BY CAST(r.submittedAt AS localdate)
        ORDER BY CAST(r.submittedAt AS localdate)
    """)
    List<GrowthDto> registrationsOverTime();
}
