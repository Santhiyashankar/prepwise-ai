package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "gd_evaluations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDiscussionEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private GroupDiscussionSession session;

    @Column(nullable = false)
    private double overallScore;

    @Column(nullable = false)
    private double communicationScore;

    @Column(nullable = false)
    private double confidenceScore;

    @Column(nullable = false)
    private double fluencyScore;

    @Column(nullable = false)
    private double grammarScore;

    @Column(nullable = false)
    private double leadershipScore;

    @Column(nullable = false)
    private double teamworkScore;

    @Column(nullable = false)
    private double criticalThinkingScore;

    @Column(nullable = false)
    private double relevanceScore;

    @Column(nullable = false)
    private double vocabularyScore;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String summary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String strengthsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String weaknessesJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String improvementTipsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String suggestedResponsesJson;

    @Column(nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @PrePersist
    protected void onCreate() {
        this.generatedAt = LocalDateTime.now();
    }
}
