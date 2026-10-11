package com.prepwise.dto.response.resume;

import com.prepwise.entity.ResumeFileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse {
    private Long id;
    private String fileName;
    private ResumeFileType fileType;
    private long fileSizeBytes;
    private int version;
    private String fullName;
    private String email;
    private String phone;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private List<String> skills;
    private List<String> education;
    private List<String> experience;
    private List<String> projects;
    private List<String> certifications;
    private List<String> languages;
    private List<String> achievements;
    private LocalDateTime uploadedAt;
}
