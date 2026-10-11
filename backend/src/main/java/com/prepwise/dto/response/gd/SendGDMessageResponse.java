package com.prepwise.dto.response.gd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendGDMessageResponse {
    private GDMessageResponse userMessage;
    private List<GDMessageResponse> aiMessages;
}
