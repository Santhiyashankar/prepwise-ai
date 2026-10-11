package com.prepwise.service;

import com.prepwise.dto.request.ChangePasswordRequest;
import com.prepwise.dto.request.UpdateNotificationPreferencesRequest;
import com.prepwise.dto.request.UpdateProfileRequest;
import com.prepwise.dto.response.AvatarUploadResponse;
import com.prepwise.dto.response.NotificationPreferenceResponse;
import com.prepwise.dto.response.ProfileExportResponse;
import com.prepwise.dto.response.ProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    ProfileResponse getProfile(String email);

    ProfileResponse updateProfile(String email, UpdateProfileRequest request);

    AvatarUploadResponse uploadAvatar(String email, MultipartFile file);

    void changePassword(String email, ChangePasswordRequest request);

    void deleteAccount(String email);

    ProfileExportResponse exportAccountData(String email);

    void logoutAllDevices(String email);

    NotificationPreferenceResponse getNotificationPreferences(String email);

    NotificationPreferenceResponse updateNotificationPreferences(
            String email, UpdateNotificationPreferencesRequest request);
}
