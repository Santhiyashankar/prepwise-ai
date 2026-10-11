package com.prepwise.dto.response.resume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnalysisResponse {
    private Long id;
    private Long resumeId;
    private double atsScore;
    private double aiScore;
    private double overallRating;
    private double keywordMatchPercentage;
    private Map<String, Double> atsBreakdown;
    private String professionalSummary;
    private String careerObjective;
    private String professionalHeadline;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> missingSections;
    private List<String> grammarSuggestions;
    private List<String> formattingSuggestions;
    private List<String> projectImprovements;
    private List<String> experienceImprovements;
    private List<String> skillSuggestions;
    private LocalDateTime analyzedAt;
}
