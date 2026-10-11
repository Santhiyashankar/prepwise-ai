package com.prepwise.controller;

import com.prepwise.dto.response.ApiResponse;
import com.prepwise.dto.response.dashboard.ActivityItemResponse;
import com.prepwise.dto.response.dashboard.DashboardProgressResponse;
import com.prepwise.dto.response.dashboard.DashboardSummaryResponse;
import com.prepwise.dto.response.dashboard.DashboardTasksResponse;
import com.prepwise.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getSummary(Authentication authentication) {
        DashboardSummaryResponse response = dashboardService.getSummary(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Dashboard summary fetched successfully", response));
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<ApiResponse<List<ActivityItemResponse>>> getRecentActivity(Authentication authentication) {
        List<ActivityItemResponse> response = dashboardService.getRecentActivity(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Recent activity fetched successfully", response));
    }

    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<DashboardProgressResponse>> getProgress(Authentication authentication) {
        DashboardProgressResponse response = dashboardService.getProgress(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Dashboard progress fetched successfully", response));
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<DashboardTasksResponse>> getTasks(Authentication authentication) {
        DashboardTasksResponse response = dashboardService.getTasks(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully", response));
    }
}
