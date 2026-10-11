package com.prepwise.dto.response.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestionResponse {
    private Long id;
    private int questionNumber;
    private int totalQuestions;
    private String questionText;
}
