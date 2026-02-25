package com.hackathon.interest;

import com.hackathon.response.Response;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "response_ai_goals",
        uniqueConstraints = @UniqueConstraint(columnNames = {"response_id", "goal"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResponseAiGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private Response response;

    @Column(nullable = false)
    private String goal;
}
