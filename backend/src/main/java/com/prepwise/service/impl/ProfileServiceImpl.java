package com.prepwise.service.impl;

import com.prepwise.dto.request.ChangePasswordRequest;
import com.prepwise.dto.request.UpdateNotificationPreferencesRequest;
import com.prepwise.dto.request.UpdateProfileRequest;
import com.prepwise.dto.response.AvatarUploadResponse;
import com.prepwise.dto.response.NotificationPreferenceResponse;
import com.prepwise.dto.response.ProfileExportResponse;
import com.prepwise.dto.response.ProfileResponse;
import com.prepwise.dto.response.UserResponse;
import com.prepwise.entity.ActivityLog;
import com.prepwise.entity.NotificationPreference;
import com.prepwise.entity.User;
import com.prepwise.entity.UserCertification;
import com.prepwise.entity.UserSkill;
import com.prepwise.exception.ApiException;
import com.prepwise.exception.DuplicateResourceException;
import com.prepwise.exception.ResourceNotFoundException;
import com.prepwise.repository.ActivityLogRepository;
import com.prepwise.repository.CompanyReadinessRepository;
import com.prepwise.repository.NotificationPreferenceRepository;
import com.prepwise.repository.PasswordResetTokenRepository;
import com.prepwise.repository.RefreshTokenRepository;
import com.prepwise.repository.SkillProgressRepository;
import com.prepwise.repository.TaskRepository;
import com.prepwise.repository.UserCertificationRepository;
import com.prepwise.repository.UserRepository;
import com.prepwise.repository.UserSkillRepository;
import com.prepwise.repository.UserStatsRepository;
import com.prepwise.service.FileStorageService;
import com.prepwise.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private static final int EXPORT_ACTIVITY_LIMIT = 50;

    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserCertificationRepository userCertificationRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ActivityLogRepository activityLogRepository;
    private final UserStatsRepository userStatsRepository;
    private final SkillProgressRepository skillProgressRepository;
    private final CompanyReadinessRepository companyReadinessRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getProfile(String email) {
        User user = resolveUser(email);
        return mapToProfileResponse(user);
    }

    @Override
    public ProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = resolveUser(email);

        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setCollege(request.getCollege());
        user.setDegree(request.getDegree());
        user.setBranch(request.getBranch());
        user.setGraduationYear(request.getGraduationYear());
        user.setCgpa(request.getCgpa());
        user.setBio(request.getBio());
        user.setLinkedinUrl(request.getLinkedinUrl());
        user.setGithubUrl(request.getGithubUrl());
        user.setPortfolioUrl(request.getPortfolioUrl());
        user.setLocation(request.getLocation());

        User savedUser = userRepository.save(user);

        replaceSkills(savedUser, request.getSkills());
        replaceCertifications(savedUser, request.getCertifications());

        return mapToProfileResponse(savedUser);
    }

    @Override
    public AvatarUploadResponse uploadAvatar(String email, MultipartFile file) {
        User user = resolveUser(email);
        String imageUrl = fileStorageService.storeAvatar(user.getId(), file);
        user.setProfileImage(imageUrl);
        userRepository.save(user);
        return AvatarUploadResponse.builder().profileImage(imageUrl).build();
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = resolveUser(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ApiException("Current password is incorrect", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        refreshTokenRepository.revokeAllByUser(user);
    }

    @Override
    public void deleteAccount(String email) {
        User user = resolveUser(email);

        refreshTokenRepository.deleteByUser(user);
        passwordResetTokenRepository.deleteByUser(user);
        activityLogRepository.deleteByUser(user);
        skillProgressRepository.deleteByUser(user);
        companyReadinessRepository.deleteByUser(user);
        taskRepository.deleteByUser(user);
        userStatsRepository.deleteByUser(user);
        notificationPreferenceRepository.deleteByUser(user);
        userSkillRepository.deleteByUser(user);
        userCertificationRepository.deleteByUser(user);

        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileExportResponse exportAccountData(String email) {
        User user = resolveUser(email);

        List<ProfileExportResponse.ActivityExportItem> recentActivity = activityLogRepository
                .findByUserOrderByCreatedAtDesc(user, PageRequest.of(0, EXPORT_ACTIVITY_LIMIT))
                .stream()
                .map(this::mapToActivityExportItem)
                .collect(Collectors.toList());

        return ProfileExportResponse.builder()
                .profile(mapToProfileResponse(user))
                .account(mapToUserResponse(user))
                .notificationPreferences(mapToNotificationResponse(resolveOrCreateNotificationPreference(user)))
                .recentActivity(recentActivity)
                .exportedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public void logoutAllDevices(String email) {
        User user = resolveUser(email);
        refreshTokenRepository.revokeAllByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationPreferenceResponse getNotificationPreferences(String email) {
        User user = resolveUser(email);
        return mapToNotificationResponse(resolveOrCreateNotificationPreference(user));
    }

    @Override
    public NotificationPreferenceResponse updateNotificationPreferences(
            String email, UpdateNotificationPreferencesRequest request) {
        User user = resolveUser(email);
        NotificationPreference preference = resolveOrCreateNotificationPreference(user);

        preference.setEmailNotifications(request.isEmailNotifications());
        preference.setInterviewReminders(request.isInterviewReminders());
        preference.setWeeklyProgressReports(request.isWeeklyProgressReports());
        preference.setPlacementAlerts(request.isPlacementAlerts());

        NotificationPreference saved = notificationPreferenceRepository.save(preference);
        return mapToNotificationResponse(saved);
    }

    private void replaceSkills(User user, List<String> skills) {
        userSkillRepository.deleteByUser(user);
        if (skills == null || skills.isEmpty()) {
            return;
        }
        List<UserSkill> entities = skills.stream()
                .filter(skill -> skill != null && !skill.isBlank())
                .map(skill -> UserSkill.builder().user(user).skillName(skill.trim()).build())
                .collect(Collectors.toList());
        userSkillRepository.saveAll(entities);
    }

    private void replaceCertifications(User user, List<String> certifications) {
        userCertificationRepository.deleteByUser(user);
        if (certifications == null || certifications.isEmpty()) {
            return;
        }
        List<UserCertification> entities = certifications.stream()
                .filter(cert -> cert != null && !cert.isBlank())
                .map(cert -> UserCertification.builder().user(user).certificationName(cert.trim()).build())
                .collect(Collectors.toList());
        userCertificationRepository.saveAll(entities);
    }

    private NotificationPreference resolveOrCreateNotificationPreference(User user) {
        return notificationPreferenceRepository.findByUser(user)
                .orElseGet(() -> notificationPreferenceRepository.save(
                        NotificationPreference.builder().user(user).build()
                ));
    }

    private ProfileResponse mapToProfileResponse(User user) {
        List<String> skills = userSkillRepository.findByUser(user).stream()
                .map(UserSkill::getSkillName)
                .collect(Collectors.toList());

        List<String> certifications = userCertificationRepository.findByUser(user).stream()
                .map(UserCertification::getCertificationName)
                .collect(Collectors.toList());

        return ProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .phone(user.getPhone())
                .college(user.getCollege())
                .degree(user.getDegree())
                .branch(user.getBranch())
                .graduationYear(user.getGraduationYear())
                .cgpa(user.getCgpa())
                .bio(user.getBio())
                .linkedinUrl(user.getLinkedinUrl())
                .githubUrl(user.getGithubUrl())
                .portfolioUrl(user.getPortfolioUrl())
                .location(user.getLocation())
                .skills(skills.isEmpty() ? Collections.emptyList() : skills)
                .certifications(certifications.isEmpty() ? Collections.emptyList() : certifications)
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

    private NotificationPreferenceResponse mapToNotificationResponse(NotificationPreference preference) {
        return NotificationPreferenceResponse.builder()
                .emailNotifications(preference.isEmailNotifications())
                .interviewReminders(preference.isInterviewReminders())
                .weeklyProgressReports(preference.isWeeklyProgressReports())
                .placementAlerts(preference.isPlacementAlerts())
                .build();
    }

    private ProfileExportResponse.ActivityExportItem mapToActivityExportItem(ActivityLog log) {
        return ProfileExportResponse.ActivityExportItem.builder()
                .activityType(log.getActivityType().name())
                .title(log.getTitle())
                .description(log.getDescription())
                .score(log.getScore())
                .createdAt(log.getCreatedAt())
                .build();
    }

    private User resolveUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
