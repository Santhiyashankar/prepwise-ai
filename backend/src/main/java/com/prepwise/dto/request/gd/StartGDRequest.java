package com.prepwise.dto.request.gd;

import com.prepwise.entity.GDCategory;
import com.prepwise.entity.GDDifficulty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartGDRequest {

    @NotNull(message = "Category is required")
    private GDCategory category;

    @NotNull(message = "Difficulty is required")
    private GDDifficulty difficulty;

    @Min(value = 3, message = "At least 3 participants are required")
    @Max(value = 6, message = "No more than 6 participants allowed")
    private int participantCount;

    @Min(value = 5, message = "Minimum duration is 5 minutes")
    @Max(value = 15, message = "Maximum duration is 15 minutes")
    private int durationMinutes;
}
