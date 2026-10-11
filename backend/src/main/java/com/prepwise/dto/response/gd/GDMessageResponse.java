package com.prepwise.dto.response.gd;

import com.prepwise.entity.GDMessageSenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GDMessageResponse {
    private Long id;
    private GDMessageSenderType senderType;
    private String senderName;
    private Long participantId;
    private int colorIndex;
    private String content;
    private int sequenceNumber;
    private LocalDateTime createdAt;
}
