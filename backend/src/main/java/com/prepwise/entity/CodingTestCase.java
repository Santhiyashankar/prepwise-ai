package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coding_test_cases")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingTestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private CodingProblem problem;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String input;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(nullable = false)
    @Builder.Default
    private boolean sample = false;

    @Column(nullable = false)
    @Builder.Default
    private int orderIndex = 0;
}
