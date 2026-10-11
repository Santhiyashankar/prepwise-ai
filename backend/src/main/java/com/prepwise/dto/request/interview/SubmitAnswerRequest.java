package com.prepwise.dto.request.interview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAnswerRequest {

    private String answerText;

    private boolean skipped;

    private Integer timeTakenSeconds;
}
