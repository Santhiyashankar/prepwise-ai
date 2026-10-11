package com.prepwise.service.impl;

import com.prepwise.dto.request.ForgotPasswordRequest;
import com.prepwise.dto.request.LoginRequest;
import com.prepwise.dto.request.RegisterRequest;
import com.prepwise.dto.request.ResetPasswordRequest;
import com.prepwise.dto.response.AuthResponse;
import com.prepwise.dto.response.UserResponse;
import com.prepwise.entity.PasswordResetToken;
import com.prepwise.entity.RefreshToken;
import com.prepwise.entity.Role;
import com.prepwise.entity.User;
import com.prepwise.entity.UserStats;
import com.prepwise.entity.NotificationPreference;
import com.prepwise.exception.DuplicateResourceException;
import com.prepwise.exception.InvalidTokenException;
import com.prepwise.exception.ResourceNotFoundException;
import com.prepwise.repository.PasswordResetTokenRepository;
import com.prepwise.repository.RefreshTokenRepository;
import com.prepwise.repository.UserRepository;
import com.prepwise.repository.UserStatsRepository;
import com.prepwise.repository.NotificationPreferenceRepository;
import com.prepwise.security.JwtUtil;
import com.prepwise.service.AuthService;
import com.prepwise.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final long PASSWORD_RESET_VALIDITY_MS = 30L * 60 * 1000;

    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .emailVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        userStatsRepository.save(
                UserStats.builder()
                        .user(savedUser)
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        notificationPreferenceRepository.save(
                NotificationPreference.builder()
                        .user(savedUser)
                        .build()
        );

        return buildAuthResponse(savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (org.springframework.security.core.AuthenticationException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse refreshToken(String refreshTokenValue) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        if (storedToken.isRevoked() || storedToken.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidTokenException("Refresh token has expired or been revoked");
        }

        User user = storedToken.getUser();

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        return buildAuthResponse(user);
    }

    @Override
    public void logout(String refreshTokenValue) {
        refreshTokenRepository.findByToken(refreshTokenValue)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToUserResponse(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            passwordResetTokenRepository.invalidateAllActiveTokensForUser(user);

            String tokenValue = jwtUtil.generateRefreshTokenValue();
            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token(tokenValue)
                    .user(user)
                    .expiryDate(Instant.now().plusMillis(PASSWORD_RESET_VALIDITY_MS))
                    .used(false)
                    .build();

            passwordResetTokenRepository.save(resetToken);
            emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName(), tokenValue);
        });
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));

        if (resetToken.isUsed() || resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidTokenException("Invalid or expired reset token");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        refreshTokenRepository.revokeAllByUser(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(user.getEmail());
        String refreshTokenValue = jwtUtil.generateRefreshTokenValue();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiryDate(Instant.now().plusMillis(jwtUtil.getRefreshTokenValidityMs()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessTokenExpirySeconds())
                .user(mapToUserResponse(user))
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .emailVerified(user.isEmailVerified())
                .build();
    }
}
