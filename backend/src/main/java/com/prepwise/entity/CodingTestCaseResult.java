package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coding_test_case_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingTestCaseResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_id", nullable = false)
    private CodingSubmission submission;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_case_id", nullable = false)
    private CodingTestCase testCase;

    @Column(nullable = false)
    private boolean passed;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String actualOutput;

    @Column
    private Long executionTimeMs;
}
