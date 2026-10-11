package com.prepwise.controller;

import com.prepwise.dto.request.coding.CodeExecutionRequest;
import com.prepwise.dto.request.coding.TimedAssessmentRequest;
import com.prepwise.dto.response.ApiResponse;
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
import com.prepwise.service.CodingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coding")
@RequiredArgsConstructor
public class CodingController {

    private final CodingService codingService;

    @GetMapping("/problems")
    public ResponseEntity<ApiResponse<List<CodingProblemSummaryResponse>>> listProblems(
            @RequestParam(required = false) CodingDifficulty difficulty,
            @RequestParam(required = false) CodingTopic topic,
            @RequestParam(required = false) CodingCompany company) {
        List<CodingProblemSummaryResponse> response = codingService.listProblems(difficulty, topic, company);
        return ResponseEntity.ok(ApiResponse.success("Problems fetched successfully", response));
    }

    @GetMapping("/problems/{slug}")
    public ResponseEntity<ApiResponse<CodingProblemDetailResponse>> getProblem(
            Authentication authentication, @PathVariable String slug) {
        CodingProblemDetailResponse response = codingService.getProblem(authentication.getName(), slug);
        return ResponseEntity.ok(ApiResponse.success("Problem fetched successfully", response));
    }

    @GetMapping("/daily-challenge")
    public ResponseEntity<ApiResponse<CodingProblemDetailResponse>> getDailyChallenge(Authentication authentication) {
        CodingProblemDetailResponse response = codingService.getDailyChallenge(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Daily challenge fetched successfully", response));
    }

    @PostMapping("/timed-assessment")
    public ResponseEntity<ApiResponse<TimedAssessmentResponse>> generateTimedAssessment(
            @Valid @RequestBody TimedAssessmentRequest request) {
        TimedAssessmentResponse response = codingService.generateTimedAssessment(request);
        return ResponseEntity.ok(ApiResponse.success("Timed assessment generated successfully", response));
    }

    @PostMapping("/run")
    public ResponseEntity<ApiResponse<RunCodeResponse>> runCode(
            Authentication authentication, @Valid @RequestBody CodeExecutionRequest request) {
        RunCodeResponse response = codingService.runCode(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success("Code executed successfully", response));
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<SubmitCodeResponse>> submitCode(
            Authentication authentication, @Valid @RequestBody CodeExecutionRequest request) {
        SubmitCodeResponse response = codingService.submitCode(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success("Solution submitted successfully", response));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<CodingHistoryItemResponse>>> getHistory(Authentication authentication) {
        List<CodingHistoryItemResponse> response = codingService.getHistory(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Coding history fetched successfully", response));
    }

    @GetMapping("/submissions/{submissionId}")
    public ResponseEntity<ApiResponse<CodingSubmissionDetailResponse>> getSubmission(
            Authentication authentication, @PathVariable Long submissionId) {
        CodingSubmissionDetailResponse response = codingService.getSubmission(authentication.getName(), submissionId);
        return ResponseEntity.ok(ApiResponse.success("Submission fetched successfully", response));
    }
}
