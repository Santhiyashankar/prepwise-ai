package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private int totalInterviews = 0;

    @Column(nullable = false)
    @Builder.Default
    private double averageScore = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private double atsResumeScore = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private double companyReadinessScore = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private int codingProblemsSolved = 0;

    @Column(nullable = false)
    @Builder.Default
    private int currentXp = 0;

    @Column(nullable = false)
    @Builder.Default
    private int currentLevel = 1;

    @Column(nullable = false)
    @Builder.Default
    private int dailyStreak = 0;

    @Column
    private LocalDate lastActivityDate;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        this.updatedAt = LocalDateTime.now();
    }
}
