package com.prepwise.dto.response.resume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeHistoryItemResponse {
    private Long id;
    private Long resumeId;
    private String fileName;
    private double atsScore;
    private double aiScore;
    private double keywordMatchPercentage;
    private LocalDateTime recordedAt;
}
