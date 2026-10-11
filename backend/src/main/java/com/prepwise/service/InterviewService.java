package com.prepwise.service;

import com.prepwise.dto.request.interview.StartInterviewRequest;
import com.prepwise.dto.request.interview.SubmitAnswerRequest;
import com.prepwise.dto.response.interview.InterviewHistoryItemResponse;
import com.prepwise.dto.response.interview.InterviewReportResponse;
import com.prepwise.dto.response.interview.InterviewSessionResponse;
import com.prepwise.dto.response.interview.SubmitAnswerResponse;

import java.util.List;

public interface InterviewService {

    InterviewSessionResponse startInterview(String email, StartInterviewRequest request);

    SubmitAnswerResponse submitAnswer(String email, Long sessionId, int questionNumber, SubmitAnswerRequest request);

    InterviewSessionResponse getSession(String email, Long sessionId);

    InterviewReportResponse getReport(String email, Long sessionId);

    List<InterviewHistoryItemResponse> getHistory(String email);

    InterviewSessionResponse retryInterview(String email, Long sessionId);

    InterviewReportResponse finishInterviewEarly(String email, Long sessionId);

    void deleteSession(String email, Long sessionId);
}
