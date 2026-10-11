package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_answers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    private InterviewQuestion question;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String answerText;

    @Column(nullable = false)
    @Builder.Default
    private boolean skipped = false;

    @Column
    private Integer timeTakenSeconds;

    @Column(nullable = false, updatable = false)
    private LocalDateTime answeredAt;

    @PrePersist
    protected void onCreate() {
        this.answeredAt = LocalDateTime.now();
    }
}
