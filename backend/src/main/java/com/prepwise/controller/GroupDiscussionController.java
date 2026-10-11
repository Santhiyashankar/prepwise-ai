package com.prepwise.controller;

import com.prepwise.dto.request.gd.SendGDMessageRequest;
import com.prepwise.dto.request.gd.StartGDRequest;
import com.prepwise.dto.response.ApiResponse;
import com.prepwise.dto.response.gd.GDHistoryItemResponse;
import com.prepwise.dto.response.gd.GDReportResponse;
import com.prepwise.dto.response.gd.GDSessionResponse;
import com.prepwise.dto.response.gd.SendGDMessageResponse;
import com.prepwise.service.GroupDiscussionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-discussions")
@RequiredArgsConstructor
public class GroupDiscussionController {

    private final GroupDiscussionService groupDiscussionService;

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<GDSessionResponse>> startDiscussion(
            Authentication authentication, @Valid @RequestBody StartGDRequest request) {
        GDSessionResponse response = groupDiscussionService.startDiscussion(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Group discussion started successfully", response));
    }

    @PostMapping("/{sessionId}/messages")
    public ResponseEntity<ApiResponse<SendGDMessageResponse>> sendMessage(
            Authentication authentication, @PathVariable Long sessionId,
            @Valid @RequestBody SendGDMessageRequest request) {
        SendGDMessageResponse response = groupDiscussionService.sendMessage(authentication.getName(), sessionId, request);
        return ResponseEntity.ok(ApiResponse.success("Message sent successfully", response));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<GDSessionResponse>> getSession(
            Authentication authentication, @PathVariable Long sessionId) {
        GDSessionResponse response = groupDiscussionService.getSession(authentication.getName(), sessionId);
        return ResponseEntity.ok(ApiResponse.success("Discussion session fetched successfully", response));
    }

    @PostMapping("/{sessionId}/finish")
    public ResponseEntity<ApiResponse<GDReportResponse>> finishDiscussion(
            Authentication authentication, @PathVariable Long sessionId) {
        GDReportResponse response = groupDiscussionService.finishDiscussion(authentication.getName(), sessionId);
        return ResponseEntity.ok(ApiResponse.success("Discussion finished successfully", response));
    }

    @GetMapping("/{sessionId}/report")
    public ResponseEntity<ApiResponse<GDReportResponse>> getReport(
            Authentication authentication, @PathVariable Long sessionId) {
        GDReportResponse response = groupDiscussionService.getReport(authentication.getName(), sessionId);
        return ResponseEntity.ok(ApiResponse.success("Discussion report fetched successfully", response));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<GDHistoryItemResponse>>> getHistory(Authentication authentication) {
        List<GDHistoryItemResponse> response = groupDiscussionService.getHistory(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Discussion history fetched successfully", response));
    }

    @PostMapping("/{sessionId}/retry")
    public ResponseEntity<ApiResponse<GDSessionResponse>> retryDiscussion(
            Authentication authentication, @PathVariable Long sessionId) {
        GDSessionResponse response = groupDiscussionService.retryDiscussion(authentication.getName(), sessionId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Discussion restarted successfully", response));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<Void>> deleteSession(
            Authentication authentication, @PathVariable Long sessionId) {
        groupDiscussionService.deleteSession(authentication.getName(), sessionId);
        return ResponseEntity.ok(ApiResponse.success("Discussion deleted successfully"));
    }
}
