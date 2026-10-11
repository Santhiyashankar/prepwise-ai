package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_preferences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private boolean emailNotifications = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean interviewReminders = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean weeklyProgressReports = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean placementAlerts = true;
}
