package com.prepwise.dto.response.coding;

import com.prepwise.entity.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunCodeResponse {
    private SubmissionStatus status;
    private String compileErrorOutput;
    private List<TestCaseRunResultResponse> results;
    private Long executionTimeMs;
}
