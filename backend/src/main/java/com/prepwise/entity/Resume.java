package com.prepwise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 255)
    private String storedFileName;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ResumeFileType fileType;

    @Column(nullable = false)
    private long fileSizeBytes;

    @Column(nullable = false)
    @Builder.Default
    private int version = 1;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String extractedText;

    @Column(length = 150)
    private String fullName;

    @Column(length = 150)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(length = 255)
    private String linkedinUrl;

    @Column(length = 255)
    private String githubUrl;

    @Column(length = 255)
    private String portfolioUrl;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String skillsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String educationJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String experienceJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String projectsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String certificationsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String languagesJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String achievementsJson;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }
}
