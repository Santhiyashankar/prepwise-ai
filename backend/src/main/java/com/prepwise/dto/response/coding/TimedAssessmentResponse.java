package com.prepwise.dto.response.coding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimedAssessmentResponse {
    private List<CodingProblemSummaryResponse> problems;
    private int totalTimeLimitMs;
}
