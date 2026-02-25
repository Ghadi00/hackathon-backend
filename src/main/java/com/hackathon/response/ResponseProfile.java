package com.hackathon.response;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "response_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResponseProfile {

    @Id
    @Column(name = "response_id")
    private String responseId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "response_id")
    private Response response;

    // Demographics
    @Column(name = "age_range")
    private String ageRange;

    @Column(name = "employment_status")
    private String employmentStatus;

    @Column(name = "job_level")
    private String jobLevel;

    @Column(name = "other_job_level")
    private String otherJobLevel;

    @Column(name = "experience_years")
    private String experienceYears;

    // Program selection
    @Column(name = "training_track", nullable = false)
    private String trainingTrack;

    @Column(name = "access_channel", nullable = false)
    private String accessChannel;

    @Column(name = "access_entity_name")
    private String accessEntityName;

    // Skill levels
    @Column(name = "digital_literacy_level")
    private String digitalLiteracyLevel;

    @Column(name = "cybersecurity_level")
    private String cybersecurityLevel;

    @Column(name = "ai_programming_level")
    private String aiProgrammingLevel;

    @Column(name = "data_skills_level")
    private String dataSkillsLevel;

    // Consents
    @Column(name = "data_processing_consent")
    private Boolean dataProcessingConsent;

    @Column(name = "follow_up_consent")
    private Boolean followUpConsent;
}
