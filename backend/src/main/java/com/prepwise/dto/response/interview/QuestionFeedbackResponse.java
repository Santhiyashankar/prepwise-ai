package com.prepwise.dto.response.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionFeedbackResponse {
    private int questionNumber;
    private String questionText;
    private String answerText;
    private boolean skipped;
    private double score;
    private List<String> strengths;
    private List<String> mistakes;
    private String idealAnswer;
}
