package com.prepwise.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNotificationPreferencesRequest {

    private boolean emailNotifications;
    private boolean interviewReminders;
    private boolean weeklyProgressReports;
    private boolean placementAlerts;
}
