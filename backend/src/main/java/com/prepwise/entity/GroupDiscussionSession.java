package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "gd_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDiscussionSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GDCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GDDifficulty difficulty;

    @Column(nullable = false)
    private int participantCount;

    @Column(nullable = false)
    private int durationMinutes;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private GDSessionStatus status = GDSessionStatus.IN_PROGRESS;

    @Column(nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        this.startedAt = LocalDateTime.now();
    }
}
