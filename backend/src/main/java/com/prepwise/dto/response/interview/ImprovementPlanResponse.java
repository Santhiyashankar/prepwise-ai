package com.prepwise.dto.response.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImprovementPlanResponse {
    private List<DailyPlanItemResponse> dailyPlan;
    private List<String> recommendedTopics;
    private List<String> projectsToRevise;
    private List<String> resumeImprovements;
}
