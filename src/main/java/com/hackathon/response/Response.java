package com.hackathon.response;

import com.hackathon.learner.Learner;
import com.hackathon.survey.Survey;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "responses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"survey_id", "learner_email"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Response {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learner_email", nullable = false)
    private Learner learner;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "geo_country")
    private String geoCountry;

    @Column(name = "geo_region")
    private String geoRegion;

    @Column(name = "geo_city")
    private String geoCity;

    @Column(name = "utm_source")
    private String utmSource;

    @Column(name = "utm_medium")
    private String utmMedium;

    @Column(name = "utm_campaign")
    private String utmCampaign;

    @Column(name = "submitted_at", nullable = false)
    private OffsetDateTime submittedAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
