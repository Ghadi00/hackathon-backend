package com.hackathon.profile;

import com.example.hackathon.response.ResponseProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProfileRepository extends JpaRepository<ResponseProfile, String> {

    // GET /profiles/admins
    @Query("""
        SELECT new com.example.hackathon.profile.AdminProfileDto(
            r.id,
            l.fullName,
            l.email,
            l.phone
        )
        FROM Response r
        JOIN r.learner l
    """)
    List<AdminProfileDto> findAdminProfiles();

    // GET /profiles/demographics
    @Query("""
        SELECT new com.example.hackathon.profile.DemographicsDto(
            r.id,
            l.email,
            rp.ageRange,
            rp.employmentStatus,
            rp.jobLevel,
            rp.otherJobLevel,
            rp.experienceYears
        )
        FROM ResponseProfile rp
        JOIN rp.response r
        JOIN r.learner l
    """)
    List<DemographicsDto> findDemographics();

    // GET /profiles/programs
    @Query("""
        SELECT new com.example.hackathon.profile.ProgramProfileDto(
            r.id,
            l.email,
            rp.trainingTrack,
            rp.accessChannel,
            rp.accessEntityName,
            rp.digitalLiteracyLevel,
            rp.cybersecurityLevel,
            rp.aiProgrammingLevel,
            rp.dataSkillsLevel,
            ps.provider,
            ps.isCompleted,
            CAST(ps.progressPct AS double)
        )
        FROM ResponseProfile rp
        JOIN rp.response r
        JOIN r.learner l
        LEFT JOIN ResponseProviderStatus ps ON ps.responseId = r.id
    """)
    List<ProgramProfileDto> findProgramProfiles();
}
