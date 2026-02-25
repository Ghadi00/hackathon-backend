package com.hackathon.interest;

import com.hackathon.response.Response;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "response_learning_reasons",
        uniqueConstraints = @UniqueConstraint(columnNames = {"response_id", "reason"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResponseLearningReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private Response response;

    @Column(nullable = false)
    private String reason;
}
