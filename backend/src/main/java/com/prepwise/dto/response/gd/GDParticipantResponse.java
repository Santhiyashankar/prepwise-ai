package com.prepwise.dto.response.gd;

import com.prepwise.entity.ParticipantPersonality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GDParticipantResponse {
    private Long id;
    private String name;
    private ParticipantPersonality personality;
    private int colorIndex;
}
