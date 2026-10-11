package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "coding_problems")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 220)
    private String slug;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CodingDifficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CodingTopic topic;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CodingCompany company;

    @Column(nullable = false)
    @Builder.Default
    private int timeLimitMs = 2000;

    @Column(nullable = false)
    @Builder.Default
    private int memoryLimitMb = 256;

    @Column(nullable = false)
    @Builder.Default
    private double acceptanceRate = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private long totalSubmissions = 0;

    @Column(nullable = false)
    @Builder.Default
    private long acceptedSubmissions = 0;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String starterCodeJava;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String starterCodePython;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String starterCodeCpp;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String starterCodeJavascript;

    @Column
    private LocalDate dailyChallengeDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
