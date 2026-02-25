package com.hackathon.interest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResponseLearningReasonRepository extends JpaRepository<ResponseLearningReason, Long> {

    @Query("""
        SELECT new com.example.hackathon.interest.MotivationDto(
            r.reason,
            COUNT(r),
            r.response.id,
            r.response.learner.fullName
        )
        FROM ResponseLearningReason r
        GROUP BY r.reason, r.response.id, r.response.learner.fullName
    """)
    List<MotivationDto> findMotivations();
}
