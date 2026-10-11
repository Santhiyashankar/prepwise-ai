package com.prepwise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private String fullName;
    private String email;
    private String profileImage;
    private String phone;
    private String college;
    private String degree;
    private String branch;
    private Integer graduationYear;
    private Double cgpa;
    private String bio;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String location;
    private List<String> skills;
    private List<String> certifications;
}
