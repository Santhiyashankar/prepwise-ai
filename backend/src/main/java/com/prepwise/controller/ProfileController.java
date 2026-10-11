package com.prepwise.controller;

import com.prepwise.dto.request.ChangePasswordRequest;
import com.prepwise.dto.request.UpdateNotificationPreferencesRequest;
import com.prepwise.dto.request.UpdateProfileRequest;
import com.prepwise.dto.response.ApiResponse;
import com.prepwise.dto.response.AvatarUploadResponse;
import com.prepwise.dto.response.NotificationPreferenceResponse;
import com.prepwise.dto.response.ProfileExportResponse;
import com.prepwise.dto.response.ProfileResponse;
import com.prepwise.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(Authentication authentication) {
        ProfileResponse response = profileService.getProfile(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            Authentication authentication, @Valid @RequestBody UpdateProfileRequest request) {
        ProfileResponse response = profileService.updateProfile(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    @PostMapping(value = "/avatar", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<AvatarUploadResponse>> uploadAvatar(
            Authentication authentication, @RequestParam("file") MultipartFile file) {
        AvatarUploadResponse response = profileService.uploadAvatar(authentication.getName(), file);
        return ResponseEntity.ok(ApiResponse.success("Profile picture updated successfully", response));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication authentication, @Valid @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteAccount(Authentication authentication) {
        profileService.deleteAccount(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully"));
    }

    @GetMapping("/export")
    public ResponseEntity<ApiResponse<ProfileExportResponse>> exportAccountData(Authentication authentication) {
        ProfileExportResponse response = profileService.exportAccountData(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Account data exported successfully", response));
    }

    @PostMapping("/logout-all-devices")
    public ResponseEntity<ApiResponse<Void>> logoutAllDevices(Authentication authentication) {
        profileService.logoutAllDevices(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Logged out from all devices"));
    }

    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> getNotificationPreferences(
            Authentication authentication) {
        NotificationPreferenceResponse response =
                profileService.getNotificationPreferences(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Notification preferences fetched successfully", response));
    }

    @PutMapping("/notifications")
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> updateNotificationPreferences(
            Authentication authentication, @RequestBody UpdateNotificationPreferencesRequest request) {
        NotificationPreferenceResponse response =
                profileService.updateNotificationPreferences(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success("Notification preferences updated successfully", response));
    }
}
