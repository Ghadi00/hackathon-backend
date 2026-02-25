package com.hackathon.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public List<AdminProfileDto> getAdminProfiles() {
        return profileRepository.findAdminProfiles();
    }

    public List<DemographicsDto> getDemographics() {
        return profileRepository.findDemographics();
    }

    public List<ProgramProfileDto> getProgramProfiles() {
        return profileRepository.findProgramProfiles();
    }
}
