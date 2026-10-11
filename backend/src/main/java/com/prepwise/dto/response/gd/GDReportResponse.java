package com.prepwise.dto.response.gd;

import com.prepwise.entity.GDCategory;
import com.prepwise.entity.GDDifficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GDReportResponse {
    private Long sessionId;
    private String topic;
    private GDCategory category;
    private GDDifficulty difficulty;
    private long durationSeconds;
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
