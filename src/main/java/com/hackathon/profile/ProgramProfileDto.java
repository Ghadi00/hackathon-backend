package com.hackathon.profile;

public record ProgramProfileDto(
        String responseId,
        String email,
        String trainingTrack,
        String accessChannel,
        String accessEntityName,
        String digitalLiteracyLevel,
        String cybersecurityLevel,
        String aiProgrammingLevel,
        String dataSkillsLevel,
        String provider,
        Boolean isCompleted,
        Double progressPct
) {}
