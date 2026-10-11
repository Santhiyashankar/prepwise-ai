package com.prepwise.dto.response.coding;

import com.prepwise.entity.CodingLanguage;
import com.prepwise.entity.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingHistoryItemResponse {
    private Long submissionId;
    private Long problemId;
    private String problemTitle;
    private String problemSlug;
    private CodingLanguage language;
    private SubmissionStatus status;
    private double score;
    private LocalDateTime submittedAt;
}
