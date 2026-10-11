package com.prepwise.controller;

import com.prepwise.dto.request.mentor.MentorChatRequest;
import com.prepwise.dto.response.ApiResponse;
import com.prepwise.service.AIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mentor")
@RequiredArgsConstructor
public class MentorController {

    private final AIService aiService;

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<String>> chat(
            Authentication authentication,
            @Valid @RequestBody MentorChatRequest request) {

        String response = aiService.chatWithMentor(request.getMessage());

        return ResponseEntity.ok(
                ApiResponse.success("Mentor response generated successfully", response)
        );
    }
}