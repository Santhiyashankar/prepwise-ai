package com.prepwise.dto.response.dashboard;

import com.prepwise.entity.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityItemResponse {
    private Long id;
    private ActivityType activityType;
    private String title;
    private String description;
    private Double score;
    private LocalDateTime createdAt;
}
