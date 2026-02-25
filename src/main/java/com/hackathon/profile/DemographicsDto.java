package com.hackathon.profile;

public record DemographicsDto(
        String responseId,
        String email,
        String ageRange,
        String employmentStatus,
        String jobLevel,
        String otherJobLevel,
        String experienceYears
) {}