package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_evaluations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    private InterviewQuestion question;

    @Column(nullable = false)
    private double score;

    @Column(nullable = false)
    @Builder.Default
    private double technicalKnowledge = 0;

    @Column(nullable = false)
    @Builder.Default
    private double communication = 0;

    @Column(nullable = false)
    @Builder.Default
    private double confidence = 0;

    @Column(nullable = false)
    @Builder.Default
    private double problemSolving = 0;

    @Column(nullable = false)
    @Builder.Default
    private double timeManagement = 0;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String strengthsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String mistakesJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String idealAnswer;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String followUpQuestion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime evaluatedAt;

    @PrePersist
    protected void onCreate() {
        this.evaluatedAt = LocalDateTime.now();
    }
}
