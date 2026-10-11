package com.prepwise.service;

import com.prepwise.dto.response.coding.CodingReportResponse;
import com.prepwise.dto.response.gd.GDEvaluationResponse;
import com.prepwise.dto.response.interview.ImprovementPlanResponse;
import com.prepwise.dto.response.interview.InterviewEvaluationResponse;
import com.prepwise.dto.response.resume.ResumeAnalysisResponse;

import java.util.List;

public interface AIService {

    ResumeAnalysisResponse analyzeResume(String resumeText);

    String generateResumeSummary(String resumeText);

    List<String> suggestResumeImprovements(String resumeText);

    String generateNextInterviewQuestion(InterviewContext context);

    InterviewEvaluationResponse evaluateInterviewAnswer(InterviewContext context, String question, String answer);

    ImprovementPlanResponse generateInterviewImprovementPlan(InterviewContext context, double overallScore);

    CodingReportResponse reviewCode(CodingReviewContext context);

    String generateGroupDiscussionTopic(String category, String difficulty);

    String generateParticipantMessage(GDContext context, String participantName, String personality);

    GDEvaluationResponse evaluateGroupDiscussion(GDContext context);
    String chatWithMentor(String message);
}
