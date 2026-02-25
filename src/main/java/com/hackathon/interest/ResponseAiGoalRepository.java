package com.hackathon.interest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResponseAiGoalRepository extends JpaRepository<ResponseAiGoal, Long> {

    @Query("""
        SELECT new com.example.hackathon.interest.GoalDemandDto(g.goal, COUNT(g))
        FROM ResponseAiGoal g
        GROUP BY g.goal
        ORDER BY COUNT(g) DESC
    """)
    List<GoalDemandDto> findGoalDemand();
}
