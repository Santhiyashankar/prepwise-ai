package com.prepwise.dto.response.interview;

import com.prepwise.entity.InterviewDifficulty;
import com.prepwise.entity.InterviewRole;
import com.prepwise.entity.InterviewSessionStatus;
import com.prepwise.entity.InterviewStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewHistoryItemResponse {
    private Long sessionId;
    private InterviewRole role;
    private InterviewStyle style;
    private InterviewDifficulty difficulty;
    private Double overallScore;
    private int totalQuestions;
    private long durationSeconds;
    private InterviewSessionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
