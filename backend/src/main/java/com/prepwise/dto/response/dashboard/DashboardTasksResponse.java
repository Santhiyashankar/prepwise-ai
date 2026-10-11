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
public class DashboardTasksResponse {
    private List<TaskItemResponse> scheduledInterviews;
    private List<TaskItemResponse> dailyGoals;
    private List<TaskItemResponse> weeklyTargets;
}
