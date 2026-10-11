package com.prepwise.dto.request.coding;

import com.prepwise.entity.CodingLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeExecutionRequest {

    @NotNull(message = "Problem id is required")
    private Long problemId;

    @NotNull(message = "Language is required")
    private CodingLanguage language;

    @NotBlank(message = "Source code is required")
    private String sourceCode;
}
