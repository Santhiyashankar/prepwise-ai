package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resume_keywords")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_analysis_id", nullable = false)
    private ResumeAnalysis resumeAnalysis;

    @Column(nullable = false, length = 100)
    private String keyword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private KeywordStatus status;
}
