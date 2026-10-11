package com.prepwise.dto.response.resume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordAnalysisResponse {
    private List<String> matchedKeywords;
    private List<String> missingKeywords;
    private List<String> weakKeywords;
    private List<String> recommendedKeywords;
    private double skillCoveragePercentage;
    private List<KeywordItemResponse> keywords;
}
