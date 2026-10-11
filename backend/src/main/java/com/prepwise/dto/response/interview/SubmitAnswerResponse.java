package com.prepwise.dto.response.interview;

import com.prepwise.entity.InterviewSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAnswerResponse {
    private InterviewEvaluationResponse evaluation;
    private InterviewQuestionResponse nextQuestion;
    private InterviewSessionStatus sessionStatus;
    private boolean interviewComplete;
}
