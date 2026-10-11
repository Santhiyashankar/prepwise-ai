package com.prepwise.dto.request.coding;

import com.prepwise.entity.CodingDifficulty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimedAssessmentRequest {

    @NotNull(message = "Difficulty is required")
    private CodingDifficulty difficulty;

    @Min(value = 1, message = "At least 1 problem is required")
    @Max(value = 10, message = "No more than 10 problems allowed")
    private int problemCount;
}
