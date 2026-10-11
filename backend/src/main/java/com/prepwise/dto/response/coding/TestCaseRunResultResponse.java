package com.prepwise.dto.response.coding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseRunResultResponse {
    private String input;
    private String expectedOutput;
    private String actualOutput;
    private boolean passed;
    private Long executionTimeMs;
}
