package com.prepwise.dto.response.gd;

import com.prepwise.entity.GDCategory;
import com.prepwise.entity.GDDifficulty;
import com.prepwise.entity.GDSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GDSessionResponse {
    private Long id;
    private GDCategory category;
    private GDDifficulty difficulty;
    private int participantCount;
    private int durationMinutes;
    private String topic;
    private GDSessionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private List<GDParticipantResponse> participants;
    private List<GDMessageResponse> messages;
}
