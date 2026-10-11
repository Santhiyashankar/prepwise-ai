package com.prepwise.service;

import com.prepwise.dto.request.coding.CodeExecutionRequest;
import com.prepwise.dto.request.coding.TimedAssessmentRequest;
import com.prepwise.dto.response.coding.CodingHistoryItemResponse;
import com.prepwise.dto.response.coding.CodingProblemDetailResponse;
import com.prepwise.dto.response.coding.CodingProblemSummaryResponse;
import com.prepwise.dto.response.coding.CodingSubmissionDetailResponse;
import com.prepwise.dto.response.coding.RunCodeResponse;
import com.prepwise.dto.response.coding.SubmitCodeResponse;
import com.prepwise.dto.response.coding.TimedAssessmentResponse;
import com.prepwise.entity.CodingCompany;
import com.prepwise.entity.CodingDifficulty;
import com.prepwise.entity.CodingTopic;

import java.util.List;

public interface CodingService {

    List<CodingProblemSummaryResponse> listProblems(CodingDifficulty difficulty, CodingTopic topic, CodingCompany company);

    CodingProblemDetailResponse getProblem(String email, String slug);

    CodingProblemDetailResponse getDailyChallenge(String email);

    TimedAssessmentResponse generateTimedAssessment(TimedAssessmentRequest request);

    RunCodeResponse runCode(String email, CodeExecutionRequest request);

    SubmitCodeResponse submitCode(String email, CodeExecutionRequest request);

    List<CodingHistoryItemResponse> getHistory(String email);

    CodingSubmissionDetailResponse getSubmission(String email, Long submissionId);
}
