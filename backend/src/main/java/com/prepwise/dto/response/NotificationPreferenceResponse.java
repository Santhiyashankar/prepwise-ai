package com.prepwise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferenceResponse {
    private boolean emailNotifications;
    private boolean interviewReminders;
    private boolean weeklyProgressReports;
    private boolean placementAlerts;
}
