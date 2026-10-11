package com.prepwise.service;

import com.prepwise.dto.response.dashboard.DashboardProgressResponse;
import com.prepwise.dto.response.dashboard.DashboardSummaryResponse;
import com.prepwise.dto.response.dashboard.DashboardTasksResponse;

import java.util.List;
import com.prepwise.dto.response.dashboard.ActivityItemResponse;

public interface DashboardService {

    DashboardSummaryResponse getSummary(String email);

    List<ActivityItemResponse> getRecentActivity(String email);

    DashboardProgressResponse getProgress(String email);

    DashboardTasksResponse getTasks(String email);
}
