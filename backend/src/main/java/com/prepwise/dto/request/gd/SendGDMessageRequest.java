package com.prepwise.dto.request.gd;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendGDMessageRequest {

    @NotBlank(message = "Message content is required")
    private String content;
}
