package com.prepwise.service.impl;

import com.prepwise.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void sendPasswordResetEmail(String toEmail, String fullName, String resetToken) {
        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Reset your PrepWise AI password");
        message.setText(
                "Hi " + fullName + ",\n\n"
                        + "We received a request to reset your PrepWise AI password.\n"
                        + "Click the link below to set a new password. This link expires in 30 minutes.\n\n"
                        + resetLink + "\n\n"
                        + "If you did not request this, you can safely ignore this email.\n\n"
                        + "— PrepWise AI"
        );

        try {
            mailSender.send(message);
        } catch (Exception ex) {
            log.error("Failed to send password reset email to {}: {}", toEmail, ex.getMessage());
        }
    }
}
