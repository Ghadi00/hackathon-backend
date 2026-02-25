package com.hackathon.provider;

import com.hackathon.response.Response;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "response_provider_status")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResponseProviderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "response_id", unique = true, nullable = false)
    private String responseId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", insertable = false, updatable = false)
    private Response response;

    @Column(nullable = false)
    private String provider;   // 'microsoft', 'oracle'

    @Column(name = "external_ref")
    private String externalRef;

    @Column(name = "progress_pct", precision = 5, scale = 2)
    private BigDecimal progressPct;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "certificate_url")
    private String certificateUrl;

    @Column(name = "synced_at")
    private OffsetDateTime syncedAt;
}
