
package com.prepwise.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private int totalInterviews;
    private double averageScore;
    private double atsResumeScore;
    private double companyReadinessScore;
    private int codingProblemsSolved;
    private int currentXp;
    private int currentLevel;
    private int dailyStreak;
}
