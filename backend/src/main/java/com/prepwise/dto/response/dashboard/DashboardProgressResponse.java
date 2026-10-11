package com.prepwise.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardProgressResponse {
    private List<WeeklyActivityPoint> weeklyActivity;
    private List<SkillProgressResponse> skillsProgress;
    private List<CompanyReadinessResponse> companyReadiness;
    private int placementReadiness;
}
