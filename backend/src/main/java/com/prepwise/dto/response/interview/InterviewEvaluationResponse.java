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
public class InterviewEvaluationResponse {
    private double score;
    private double technicalKnowledge;
    private double communication;
    private double confidence;
    private double problemSolving;
    private double timeManagement;
    private List<String> strengths;
    private List<String> mistakes;
    private String idealAnswer;
    private String followUpQuestion;
}
