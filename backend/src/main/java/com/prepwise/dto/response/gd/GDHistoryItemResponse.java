package com.prepwise.dto.response.gd;

import com.prepwise.entity.GDCategory;
import com.prepwise.entity.GDDifficulty;
import com.prepwise.entity.GDSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GDHistoryItemResponse {
    private Long sessionId;
    private String topic;
    private GDCategory category;
    private GDDifficulty difficulty;
    private GDSessionStatus status;
    private Double overallScore;
    private long durationSeconds;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
