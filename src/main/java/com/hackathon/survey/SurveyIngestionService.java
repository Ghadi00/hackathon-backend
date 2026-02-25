package com.hackathon.survey;

import com.hackathon.interest.ResponseAiGoal;
import com.hackathon.interest.ResponseAiGoalRepository;
import com.hackathon.interest.ResponseLearningReason;
import com.hackathon.interest.ResponseLearningReasonRepository;
import com.hackathon.learner.Learner;
import com.hackathon.learner.LearnerRepository;
import com.hackathon.response.Response;
import com.hackathon.response.ResponseProfile;
import com.hackathon.response.ResponseProfileRepository;
import com.hackathon.response.ResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyIngestionService {

    private final RestTemplate restTemplate;
    private final SurveyRepository surveyRepository;
    private final LearnerRepository learnerRepository;
    private final ResponseRepository responseRepository;
    private final ResponseProfileRepository responseProfileRepository;
    private final ResponseAiGoalRepository aiGoalRepository;
    private final ResponseLearningReasonRepository learningReasonRepository;

    @Value("${survey.api.base-url}")
    private String apiBaseUrl;

    @Value("${survey.api.survey-id}")
    private String surveyId;

    // Run on startup and then every hour
    @Scheduled(fixedRateString = "3600000", initialDelay = 0)
    public void ingest() {
        log.info("Starting survey ingestion...");
        try {
            SurveyApiResponse apiResponse = restTemplate.getForObject(
                    apiBaseUrl + "/surveys/" + surveyId + "/responses",
                    SurveyApiResponse.class
            );

            if (apiResponse == null || !apiResponse.isSuccess()) {
                log.warn("Survey API returned null or unsuccessful response");
                return;
            }

            List<SurveyApiResponse.ApiResponse> responses = apiResponse.getData().getResponses();
            log.info("Fetched {} responses from survey API", responses.size());

            for (SurveyApiResponse.ApiResponse apiResp : responses) {
                try {
                    ingestSingleResponse(apiResp);
                } catch (Exception e) {
                    log.error("Failed to ingest response {}: {}", apiResp.getId(), e.getMessage());
                }
            }

            log.info("Ingestion complete.");
        } catch (Exception e) {
            log.error("Survey ingestion failed: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void ingestSingleResponse(SurveyApiResponse.ApiResponse apiResp) {
        // Skip if already ingested
        if (responseRepository.existsById(apiResp.getId())) {
            return;
        }

        Survey survey = surveyRepository.findById(apiResp.getSurveyId())
                .orElseThrow(() -> new IllegalStateException("Survey not found: " + apiResp.getSurveyId()));

        // 1. Upsert learner (email is the key)
        Learner learner = learnerRepository.findById(apiResp.getRespondentEmail())
                .orElseGet(() -> Learner.builder()
                        .email(apiResp.getRespondentEmail())
                        .createdAt(OffsetDateTime.now())
                        .build());
        learner.setFullName(apiResp.getRespondentName());
        learner.setPhone(apiResp.getRespondentPhone());
        learnerRepository.save(learner);

        // 2. Insert response (metadata row)
        Response response = Response.builder()
                .id(apiResp.getId())
                .survey(survey)
                .learner(learner)
                .ipAddress(apiResp.getIpAddress())
                .geoCountry(apiResp.getGeoCountry())
                .geoRegion(apiResp.getGeoRegion())
                .geoCity(apiResp.getGeoCity())
                .utmSource(apiResp.getUtmSource())
                .utmMedium(apiResp.getUtmMedium())
                .utmCampaign(apiResp.getUtmCampaign())
                .submittedAt(OffsetDateTime.parse(apiResp.getCreatedAt()))
                .createdAt(OffsetDateTime.now())
                .build();
        responseRepository.save(response);

        // 3. Insert response profile
        SurveyApiResponse.ResponseFields fields = apiResp.getResponses();
        ResponseProfile profile = ResponseProfile.builder()
                .responseId(response.getId())
                .response(response)
                .ageRange(fields.getAgeRange())
                .employmentStatus(fields.getEmploymentStatus())
                .jobLevel(fields.getJobLevel())
                .otherJobLevel(fields.getOtherJobLevel())
                .experienceYears(fields.getExperienceYears())
                .trainingTrack(fields.getTrainingTrack())
                .accessChannel(fields.getAccessChannel())
                .accessEntityName(fields.resolveEntityName())
                .digitalLiteracyLevel(fields.getDigitalLiteracyLevel())
                .cybersecurityLevel(fields.getCybersecurityLevel())
                .aiProgrammingLevel(fields.getAiProgrammingLevel())
                .dataSkillsLevel(fields.getDataSkillsLevel())
                .dataProcessingConsent(fields.getDataProcessingConsent())
                .followUpConsent(fields.getFollowUpConsent())
                .build();
        responseProfileRepository.save(profile);

        // 4. Insert AI goals (one row per goal)
        if (fields.getAiGoals() != null) {
            for (String goal : fields.getAiGoals()) {
                aiGoalRepository.save(
                        ResponseAiGoal.builder()
                                .response(response)
                                .goal(goal)
                                .build()
                );
            }
        }

        // 5. Insert learning reasons (one row per reason)
        if (fields.getLearningReason() != null) {
            for (String reason : fields.getLearningReason()) {
                learningReasonRepository.save(
                        ResponseLearningReason.builder()
                                .response(response)
                                .reason(reason)
                                .build()
                );
            }
        }
    }
}
