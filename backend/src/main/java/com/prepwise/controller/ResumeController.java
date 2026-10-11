package com.prepwise.controller;

import com.prepwise.dto.request.AnalyzeResumeRequest;
import com.prepwise.dto.response.ApiResponse;
import com.prepwise.dto.response.resume.ResumeComparisonResponse;
import com.prepwise.dto.response.resume.ResumeFullResponse;
import com.prepwise.dto.response.resume.ResumeHistoryItemResponse;
import com.prepwise.dto.response.resume.ResumeResponse;
import com.prepwise.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ResumeResponse>> uploadResume(
            Authentication authentication, @RequestParam("file") MultipartFile file) {
        ResumeResponse response = resumeService.uploadResume(authentication.getName(), file);
        return ResponseEntity.ok(ApiResponse.success("Resume uploaded successfully", response));
    }

    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<ResumeFullResponse>> analyzeResume(
            Authentication authentication, @Valid @RequestBody AnalyzeResumeRequest request) {
        ResumeFullResponse response = resumeService.analyzeResume(authentication.getName(), request.getResumeId());
        return ResponseEntity.ok(ApiResponse.success("Resume analyzed successfully", response));
    }

    @PostMapping("/reanalyze")
    public ResponseEntity<ApiResponse<ResumeFullResponse>> reanalyzeResume(
            Authentication authentication, @Valid @RequestBody AnalyzeResumeRequest request) {
        ResumeFullResponse response = resumeService.reanalyzeResume(authentication.getName(), request.getResumeId());
        return ResponseEntity.ok(ApiResponse.success("Resume re-analyzed successfully", response));
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<ResumeFullResponse>> getLatestResume(Authentication authentication) {
        ResumeFullResponse response = resumeService.getLatestResume(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Latest resume fetched successfully", response));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<ResumeHistoryItemResponse>>> getHistory(Authentication authentication) {
        List<ResumeHistoryItemResponse> response = resumeService.getHistory(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Resume history fetched successfully", response));
    }

    @GetMapping("/compare")
    public ResponseEntity<ApiResponse<ResumeComparisonResponse>> compareResumes(
            Authentication authentication,
            @RequestParam("firstId") Long firstId,
            @RequestParam("secondId") Long secondId) {
        ResumeComparisonResponse response =
                resumeService.compareResumes(authentication.getName(), firstId, secondId);
        return ResponseEntity.ok(ApiResponse.success("Resumes compared successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeFullResponse>> getResumeById(
            Authentication authentication, @PathVariable Long id) {
        ResumeFullResponse response = resumeService.getResumeById(authentication.getName(), id);
        return ResponseEntity.ok(ApiResponse.success("Resume fetched successfully", response));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadResume(Authentication authentication, @PathVariable Long id) {
        byte[] fileBytes = resumeService.downloadResume(authentication.getName(), id);
        String fileName = resumeService.getResumeFileName(authentication.getName(), id);
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(fileBytes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(Authentication authentication, @PathVariable Long id) {
        resumeService.deleteResume(authentication.getName(), id);
        return ResponseEntity.ok(ApiResponse.success("Resume deleted successfully"));
    }
}
