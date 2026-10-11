package com.prepwise.dto.response.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlanItemResponse {
    private int day;
    private String focusArea;
    private List<String> tasks;
}
