package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InterviewRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InterviewDifficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InterviewExperience experience;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InterviewStyle style;

    @Column(nullable = false)
    private int totalQuestions;

    @Column(nullable = false)
    @Builder.Default
    private int currentQuestionNumber = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private InterviewSessionStatus status = InterviewSessionStatus.IN_PROGRESS;

    @Column
    private Double overallScore;

    @Column
    private Double technicalKnowledgeScore;

    @Column
    private Double communicationScore;

    @Column
    private Double confidenceScore;

    @Column
    private Double problemSolvingScore;

    @Column
    private Double timeManagementScore;

    @Column(nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        this.startedAt = LocalDateTime.now();
    }
}
