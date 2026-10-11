package com.prepwise.dto.request.interview;

import com.prepwise.entity.InterviewDifficulty;
import com.prepwise.entity.InterviewExperience;
import com.prepwise.entity.InterviewRole;
import com.prepwise.entity.InterviewStyle;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartInterviewRequest {

    @NotNull(message = "Role is required")
    private InterviewRole role;

    @NotNull(message = "Difficulty is required")
    private InterviewDifficulty difficulty;

    @NotNull(message = "Experience is required")
    private InterviewExperience experience;

    @NotNull(message = "Interview style is required")
    private InterviewStyle style;

    @Min(value = 5, message = "Question count must be at least 5")
    @Max(value = 15, message = "Question count cannot exceed 15")
    private int questionCount;
}
