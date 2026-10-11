package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resume_analyses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false)
    private double atsScore;

    @Column(nullable = false)
    private double aiScore;

    @Column(nullable = false)
    private double overallRating;

    @Column(nullable = false)
    private double keywordMatchPercentage;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String atsCriteriaJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String professionalSummary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String careerObjective;

    @Column(length = 255)
    private String professionalHeadline;

    @Column(nullable = false, updatable = false)
    private LocalDateTime analyzedAt;

    @PrePersist
    protected void onCreate() {
        this.analyzedAt = LocalDateTime.now();
    }
}
