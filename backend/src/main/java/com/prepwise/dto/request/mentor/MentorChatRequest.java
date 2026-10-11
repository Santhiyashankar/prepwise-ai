package com.prepwise.dto.request.mentor;



import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MentorChatRequest {

    @NotBlank(message = "Message cannot be empty")
    private String message;
}