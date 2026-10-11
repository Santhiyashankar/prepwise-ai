package com.prepwise.dto.response.resume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeFullResponse {
    private ResumeResponse resume;
    private ResumeAnalysisResponse analysis;
    private KeywordAnalysisResponse keywordAnalysis;
}
