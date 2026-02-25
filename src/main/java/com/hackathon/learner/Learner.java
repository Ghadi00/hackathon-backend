package com.hackathon.learner;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "learners")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Learner {

    @Id
    private String email;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
