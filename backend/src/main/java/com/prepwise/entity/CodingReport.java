package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coding_reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_id", nullable = false, unique = true)
    private CodingSubmission submission;

    @Column(nullable = false)
    private double overallScore;

    @Column(nullable = false)
    private double correctnessScore;

    @Column(nullable = false)
    private double readabilityScore;

    @Column(nullable = false)
    private double bestPracticesScore;

    @Column(nullable = false)
    private double edgeCaseScore;

    @Column(length = 100)
    private String timeComplexity;

    @Column(length = 100)
    private String spaceComplexity;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String strengthsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String weaknessesJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String optimizationSuggestionsJson;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String optimizedSolution;

    @Column(nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @PrePersist
    protected void onCreate() {
        this.generatedAt = LocalDateTime.now();
    }
}
