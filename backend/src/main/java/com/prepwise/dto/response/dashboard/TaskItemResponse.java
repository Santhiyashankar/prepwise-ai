package com.prepwise.dto.response.dashboard;

import com.prepwise.entity.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskItemResponse {
    private Long id;
    private TaskType taskType;
    private String title;
    private LocalDateTime dueDate;
    private boolean completed;
}
