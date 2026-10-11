package com.prepwise.dto.response.coding;

import com.prepwise.entity.CodingLanguage;
import com.prepwise.entity.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingSubmissionDetailResponse {
    private Long submissionId;
    private Long problemId;
    private String problemTitle;
    private CodingLanguage language;
    private String sourceCode;
    private SubmissionStatus status;
    private double score;
    private int passedTestCases;
    private int totalTestCases;
    private Long executionTimeMs;
    private Long memoryUsageKb;
    private String compileErrorOutput;
    private List<TestCaseRunResultResponse> testCaseResults;
    private CodingReportResponse report;
    private LocalDateTime submittedAt;
}
