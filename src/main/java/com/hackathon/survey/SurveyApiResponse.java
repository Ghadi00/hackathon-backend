package com.hackathon.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SurveyApiResponse {
    private boolean success;
    private Data data;

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private List<ApiResponse> responses;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiResponse {
        private String id;

        @JsonProperty("survey_id")
        private String surveyId;

        @JsonProperty("respondent_email")
        private String respondentEmail;

        @JsonProperty("respondent_phone")
        private String respondentPhone;

        @JsonProperty("respondent_name")
        private String respondentName;

        private ResponseFields responses;

        @JsonProperty("ip_address")
        private String ipAddress;

        @JsonProperty("geo_country")
        private String geoCountry;

        @JsonProperty("geo_region")
        private String geoRegion;

        @JsonProperty("geo_city")
        private String geoCity;

        @JsonProperty("utm_source")
        private String utmSource;

        @JsonProperty("utm_medium")
        private String utmMedium;

        @JsonProperty("utm_campaign")
        private String utmCampaign;

        @JsonProperty("created_at")
        private String createdAt;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseFields {
        @JsonProperty("age_range")
        private String ageRange;

        @JsonProperty("employment_status")
        private String employmentStatus;

        @JsonProperty("job_level")
        private String jobLevel;

        @JsonProperty("other_job_level")
        private String otherJobLevel;

        @JsonProperty("experience_years")
        private String experienceYears;

        @JsonProperty("training_track")
        private String trainingTrack;

        @JsonProperty("access_channel")
        private String accessChannel;

        // Conditional entity name fields — resolved at ingestion time
        @JsonProperty("university_name")
        private String universityName;

        @JsonProperty("employer_name")
        private String employerName;

        @JsonProperty("ngo_name")
        private String ngoName;

        @JsonProperty("public_sector_name")
        private String publicSectorName;

        @JsonProperty("other_access_channel")
        private String otherAccessChannel;

        @JsonProperty("digital_literacy_level")
        private String digitalLiteracyLevel;

        @JsonProperty("cybersecurity_level")
        private String cybersecurityLevel;

        @JsonProperty("ai_programming_level")
        private String aiProgrammingLevel;

        @JsonProperty("data_skills_level")
        private String dataSkillsLevel;

        @JsonProperty("learning_reason")
        private List<String> learningReason;

        @JsonProperty("ai_goals")
        private List<String> aiGoals;

        @JsonProperty("data_processing_consent")
        private Boolean dataProcessingConsent;

        @JsonProperty("follow_up_consent")
        private Boolean followUpConsent;

        // Resolves the correct entity name based on access channel
        public String resolveEntityName() {
            if (universityName != null)    return universityName;
            if (employerName != null)      return employerName;
            if (ngoName != null)           return ngoName;
            if (publicSectorName != null)  return publicSectorName;
            if (otherAccessChannel != null) return otherAccessChannel;
            return null;
        }
    }
}
