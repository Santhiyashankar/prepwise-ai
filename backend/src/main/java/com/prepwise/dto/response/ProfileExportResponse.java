package com.prepwise.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileExportResponse {

    private ProfileResponse profile;
    private UserResponse account;
    private NotificationPreferenceResponse notificationPreferences;
    private List<ActivityExportItem> recentActivity;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime exportedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityExportItem {
        private String activityType;
        private String title;
        private String description;
        private Double score;
        private LocalDateTime createdAt;
    }
}
