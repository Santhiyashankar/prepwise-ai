package com.prepwise.controller;

import com.prepwise.dto.request.interview.StartInterviewRequest;
import com.prepwise.dto.request.interview.SubmitAnswerRequest;
import com.prepwise.dto.response.ApiResponse;
import com.prepwise.dto.response.interview.InterviewHistoryItemResponse;
import com.prepwise.dto.response.interview.InterviewReportResponse;
import com.prepwise.dto.response.interview.InterviewSessionResponse;
import com.prepwise.dto.response.interview.SubmitAnswerResponse;
import com.prepwise.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<InterviewSessionResponse>> startInterview(
            Authentication authentication, @Valid @RequestBody StartInterviewRequest request) {
        InterviewSessionResponse response = interviewService.startInterview(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Interview started successfully", response));
    }

    @PostMapping("/{sessionId}/questions/{questionNumber}/answer")
    public ResponseEntity<ApiResponse<SubmitAnswerResponse>> submitAnswer(
            Authentication authentication,
            @PathVariable Long sessionId,
            @PathVariable int questionNumber,
            @RequestBody SubmitAnswerRequest request) {
        SubmitAnswerResponse response =
                interviewService.submitAnswer(authentication.getName(), sessionId, questionNumber, request);
        return ResponseEntity.ok(ApiResponse.success("Answer submitted successfully", response));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<InterviewSessionResponse>> getSession(
            Authentication authentication, @PathVariable Long sessionId) {
        InterviewSessionResponse response = interviewService.getSession(authentication.getName(), sessionId);
        return ResponseEntity.ok(ApiResponse.success("Interview session fetched successfully", response));
    }

    @GetMapping("/{sessionId}/report")
    public ResponseEntity<ApiResponse<InterviewReportResponse>> getReport(
            Authentication authentication, @PathVariable Long sessionId) {
        InterviewReportResponse response = interviewService.getReport(authentication.getName(), sessionId);
        return ResponseEntity.ok(ApiResponse.success("Interview report fetched successfully", response));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<InterviewHistoryItemResponse>>> getHistory(Authentication authentication) {
        List<InterviewHistoryItemResponse> response = interviewService.getHistory(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Interview history fetched successfully", response));
    }

    @PostMapping("/{sessionId}/retry")
    public ResponseEntity<ApiResponse<InterviewSessionResponse>> retryInterview(
            Authentication authentication, @PathVariable Long sessionId) {
        InterviewSessionResponse response = interviewService.retryInterview(authentication.getName(), sessionId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Interview restarted successfully", response));
    }

    @PostMapping("/{sessionId}/finish")
    public ResponseEntity<ApiResponse<InterviewReportResponse>> finishInterview(
            Authentication authentication, @PathVariable Long sessionId) {
        InterviewReportResponse response = interviewService.finishInterviewEarly(authentication.getName(), sessionId);
        return ResponseEntity.ok(ApiResponse.success("Interview finished successfully", response));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<Void>> deleteSession(
            Authentication authentication, @PathVariable Long sessionId) {
        interviewService.deleteSession(authentication.getName(), sessionId);
        return ResponseEntity.ok(ApiResponse.success("Interview deleted successfully"));
    }
}
