package com.prepwise.service;

public interface EmailService {

    void sendPasswordResetEmail(String toEmail, String fullName, String resetToken);
}
