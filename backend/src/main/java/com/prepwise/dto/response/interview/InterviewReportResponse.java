package com.prepwise.dto.response.interview;

import com.prepwise.entity.InterviewDifficulty;
import com.prepwise.entity.InterviewRole;
import com.prepwise.entity.InterviewStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewReportResponse {
    private Long sessionId;
    private InterviewRole role;
    private InterviewStyle style;
    private InterviewDifficulty difficulty;
    private double overallScore;
    private double technicalKnowledge;
    private double communication;
    private double confidence;
    private double problemSolving;
    private double timeManagement;
    private long durationSeconds;
    private String summary;
    private List<QuestionFeedbackResponse> questionFeedback;
    private ImprovementPlanResponse improvementPlan;
    private List<String> badges;
}
