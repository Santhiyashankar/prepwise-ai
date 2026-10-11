package com.prepwise.dto.response.interview;

import com.prepwise.entity.InterviewDifficulty;
import com.prepwise.entity.InterviewExperience;
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
public class InterviewSessionResponse {
    private Long id;
    private InterviewRole role;
    private InterviewDifficulty difficulty;
    private InterviewExperience experience;
    private InterviewStyle style;
    private int totalQuestions;
    private int currentQuestionNumber;
    private InterviewSessionStatus status;
    private Double overallScore;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private InterviewQuestionResponse firstQuestion;
}
