package com.prepwise.dto.response.gd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GDEvaluationResponse {
    private double overallScore;
    private double communicationScore;
    private double confidenceScore;
    private double fluencyScore;
    private double grammarScore;
    private double leadershipScore;
    private double teamworkScore;
    private double criticalThinkingScore;
    private double relevanceScore;
    private double vocabularyScore;
    private String summary;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> improvementTips;
    private List<String> suggestedResponses;
}
