package com.prepwise.service;

import com.prepwise.dto.request.ForgotPasswordRequest;
import com.prepwise.dto.request.LoginRequest;
import com.prepwise.dto.request.RegisterRequest;
import com.prepwise.dto.request.ResetPasswordRequest;
import com.prepwise.dto.response.AuthResponse;
import com.prepwise.dto.response.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

    UserResponse getCurrentUser(String email);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}