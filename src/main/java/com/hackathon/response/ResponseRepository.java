package com.hackathon.response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, String> {
    boolean existsByIdAndSurveyId(String id, String surveyId);
}
