package com.prepwise.dto.response.resume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeComparisonResponse {
    private ResumeFullResponse first;
    private ResumeFullResponse second;
}
