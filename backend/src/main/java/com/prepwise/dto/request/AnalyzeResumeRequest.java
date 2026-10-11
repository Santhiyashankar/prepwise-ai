package com.prepwise.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzeResumeRequest {

    @NotNull(message = "Resume id is required")
    private Long resumeId;
}
