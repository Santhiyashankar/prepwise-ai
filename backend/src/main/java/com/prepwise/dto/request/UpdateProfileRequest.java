package com.prepwise.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^$|^[+0-9 ()-]{7,20}$", message = "Enter a valid phone number")
    private String phone;

    @Size(max = 150, message = "College name is too long")
    private String college;

    @Size(max = 100, message = "Degree is too long")
    private String degree;

    @Size(max = 100, message = "Branch is too long")
    private String branch;

    @Min(value = 1950, message = "Enter a valid graduation year")
    @Max(value = 2100, message = "Enter a valid graduation year")
    private Integer graduationYear;

    @DecimalMin(value = "0.0", message = "CGPA must be positive")
    @DecimalMax(value = "10.0", message = "CGPA cannot exceed 10.0")
    private Double cgpa;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    @Size(max = 255, message = "LinkedIn URL is too long")
    private String linkedinUrl;

    @Size(max = 255, message = "GitHub URL is too long")
    private String githubUrl;

    @Size(max = 255, message = "Portfolio URL is too long")
    private String portfolioUrl;

    @Size(max = 150, message = "Location is too long")
    private String location;

    private List<@Size(max = 100) String> skills;

    private List<@Size(max = 150) String> certifications;
}
