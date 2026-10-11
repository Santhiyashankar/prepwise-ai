package com.prepwise.dto.response.coding;

import com.prepwise.entity.CodingCompany;
import com.prepwise.entity.CodingDifficulty;
import com.prepwise.entity.CodingTopic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingProblemSummaryResponse {
    private Long id;
    private String title;
    private String slug;
    private CodingDifficulty difficulty;
    private CodingTopic topic;
    private CodingCompany company;
    private double acceptanceRate;
    private int timeLimitMs;
}
