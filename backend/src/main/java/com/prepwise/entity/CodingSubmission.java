package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coding_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private CodingProblem problem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CodingLanguage language;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SubmissionStatus status = SubmissionStatus.PENDING;

    @Column(nullable = false)
    @Builder.Default
    private double score = 0;

    @Column(nullable = false)
    @Builder.Default
    private int passedTestCases = 0;

    @Column(nullable = false)
    @Builder.Default
    private int totalTestCases = 0;

    @Column
    private Long executionTimeMs;

    @Column
    private Long memoryUsageKb;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String compileErrorOutput;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    protected void onCreate() {
        this.submittedAt = LocalDateTime.now();
    }
}
