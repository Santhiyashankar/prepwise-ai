package com.prepwise.dto.response.coding;

import com.prepwise.entity.CodingCompany;
import com.prepwise.entity.CodingDifficulty;
import com.prepwise.entity.CodingTopic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingProblemDetailResponse {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private CodingDifficulty difficulty;
    private CodingTopic topic;
    private CodingCompany company;
    private int timeLimitMs;
    private int memoryLimitMb;
    private double acceptanceRate;
    private Map<String, String> starterCode;
    private List<TestCaseSampleResponse> sampleTestCases;
    private Double lastScore;
}
