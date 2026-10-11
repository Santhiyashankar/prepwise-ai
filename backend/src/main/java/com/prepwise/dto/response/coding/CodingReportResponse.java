package com.prepwise.dto.response.coding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingReportResponse {
    private double overallScore;
    private double correctnessScore;
    private double readabilityScore;
    private double bestPracticesScore;
    private double edgeCaseScore;
    private String timeComplexity;
    private String spaceComplexity;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> optimizationSuggestions;
    private String optimizedSolution;
}
