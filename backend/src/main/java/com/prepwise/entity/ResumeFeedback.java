package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resume_feedback")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_analysis_id", nullable = false)
    private ResumeAnalysis resumeAnalysis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private FeedbackCategory category;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}
