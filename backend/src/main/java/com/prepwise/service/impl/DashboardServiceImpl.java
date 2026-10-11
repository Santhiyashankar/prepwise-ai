package com.prepwise.service.impl;

import com.prepwise.dto.response.dashboard.ActivityItemResponse;
import com.prepwise.dto.response.dashboard.CompanyReadinessResponse;
import com.prepwise.dto.response.dashboard.DashboardProgressResponse;
import com.prepwise.dto.response.dashboard.DashboardSummaryResponse;
import com.prepwise.dto.response.dashboard.DashboardTasksResponse;
import com.prepwise.dto.response.dashboard.SkillProgressResponse;
import com.prepwise.dto.response.dashboard.TaskItemResponse;
import com.prepwise.dto.response.dashboard.WeeklyActivityPoint;
import com.prepwise.entity.ActivityLog;
import com.prepwise.entity.ActivityType;
import com.prepwise.entity.TaskType;
import com.prepwise.entity.User;
import com.prepwise.entity.UserStats;
import com.prepwise.exception.ResourceNotFoundException;
import com.prepwise.repository.ActivityLogRepository;
import com.prepwise.repository.CompanyReadinessRepository;
import com.prepwise.repository.SkillProgressRepository;
import com.prepwise.repository.TaskRepository;
import com.prepwise.repository.UserRepository;
import com.prepwise.repository.UserStatsRepository;
import com.prepwise.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private static final int RECENT_ACTIVITY_LIMIT = 10;
    private static final int WEEKLY_WINDOW_DAYS = 7;

    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;
    private final ActivityLogRepository activityLogRepository;
    private final SkillProgressRepository skillProgressRepository;
    private final CompanyReadinessRepository companyReadinessRepository;
    private final TaskRepository taskRepository;

    @Override
    public DashboardSummaryResponse getSummary(String email) {
        UserStats stats = resolveUserStats(email);

        return DashboardSummaryResponse.builder()
                .totalInterviews(stats.getTotalInterviews())
                .averageScore(stats.getAverageScore())
                .atsResumeScore(stats.getAtsResumeScore())
                .companyReadinessScore(stats.getCompanyReadinessScore())
                .codingProblemsSolved(stats.getCodingProblemsSolved())
                .currentXp(stats.getCurrentXp())
                .currentLevel(stats.getCurrentLevel())
                .dailyStreak(stats.getDailyStreak())
                .build();
    }

    @Override
    public List<ActivityItemResponse> getRecentActivity(String email) {
        User user = resolveUser(email);

        return activityLogRepository
                .findByUserOrderByCreatedAtDesc(user, PageRequest.of(0, RECENT_ACTIVITY_LIMIT))
                .stream()
                .map(this::mapToActivityItem)
                .collect(Collectors.toList());
    }

    @Override
    public DashboardProgressResponse getProgress(String email) {
        User user = resolveUser(email);
        UserStats stats = resolveUserStats(email);

        List<WeeklyActivityPoint> weeklyActivity = buildWeeklyActivity(user);

        List<SkillProgressResponse> skillsProgress = skillProgressRepository.findByUser(user).stream()
                .map(skill -> SkillProgressResponse.builder()
                        .skillName(skill.getSkillName())
                        .progressPercentage(skill.getProgressPercentage())
                        .build())
                .collect(Collectors.toList());

        List<CompanyReadinessResponse> companyReadiness = companyReadinessRepository.findByUser(user).stream()
                .map(readiness -> CompanyReadinessResponse.builder()
                        .companyName(readiness.getCompanyName())
                        .readinessPercentage(readiness.getReadinessPercentage())
                        .build())
                .collect(Collectors.toList());

        int placementReadiness = computePlacementReadiness(stats);

        return DashboardProgressResponse.builder()
                .weeklyActivity(weeklyActivity)
                .skillsProgress(skillsProgress)
                .companyReadiness(companyReadiness)
                .placementReadiness(placementReadiness)
                .build();
    }

    @Override
    public DashboardTasksResponse getTasks(String email) {
        User user = resolveUser(email);

        List<TaskItemResponse> scheduledInterviews = taskRepository
                .findByUserAndTaskTypeAndCompletedFalseOrderByDueDateAsc(user, TaskType.SCHEDULED_INTERVIEW)
                .stream()
                .map(this::mapToTaskItem)
                .collect(Collectors.toList());

        List<TaskItemResponse> dailyGoals = taskRepository
                .findByUserAndTaskTypeAndCompletedFalseOrderByDueDateAsc(user, TaskType.DAILY_GOAL)
                .stream()
                .map(this::mapToTaskItem)
                .collect(Collectors.toList());

        List<TaskItemResponse> weeklyTargets = taskRepository
                .findByUserAndTaskTypeAndCompletedFalseOrderByDueDateAsc(user, TaskType.WEEKLY_TARGET)
                .stream()
                .map(this::mapToTaskItem)
                .collect(Collectors.toList());

        return DashboardTasksResponse.builder()
                .scheduledInterviews(scheduledInterviews)
                .dailyGoals(dailyGoals)
                .weeklyTargets(weeklyTargets)
                .build();
    }

    private List<WeeklyActivityPoint> buildWeeklyActivity(User user) {
        LocalDate today = LocalDate.now();
        LocalDate windowStart = today.minusDays(WEEKLY_WINDOW_DAYS - 1L);

        List<ActivityLog> interviewLogs = activityLogRepository
                .findByUserAndCreatedAtAfterOrderByCreatedAtAsc(user, windowStart.atStartOfDay())
                .stream()
                .filter(log -> log.getActivityType() == ActivityType.INTERVIEW)
                .collect(Collectors.toList());

        Map<LocalDate, Long> countsByDate = interviewLogs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getCreatedAt().toLocalDate(),
                        Collectors.counting()
                ));

        List<WeeklyActivityPoint> points = new ArrayList<>();
        for (int i = 0; i < WEEKLY_WINDOW_DAYS; i++) {
            LocalDate date = windowStart.plusDays(i);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            long count = countsByDate.getOrDefault(date, 0L);

            points.add(WeeklyActivityPoint.builder()
                    .day(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH))
                    .interviewCount((int) count)
                    .build());
        }
        return points;
    }

    private int computePlacementReadiness(UserStats stats) {
        double composite = (stats.getAverageScore() + stats.getAtsResumeScore() + stats.getCompanyReadinessScore()) / 3.0;
        return (int) Math.round(Math.max(0, Math.min(100, composite)));
    }

    private ActivityItemResponse mapToActivityItem(ActivityLog log) {
        return ActivityItemResponse.builder()
                .id(log.getId())
                .activityType(log.getActivityType())
                .title(log.getTitle())
                .description(log.getDescription())
                .score(log.getScore())
                .createdAt(log.getCreatedAt())
                .build();
    }

    private TaskItemResponse mapToTaskItem(com.prepwise.entity.Task task) {
        return TaskItemResponse.builder()
                .id(task.getId())
                .taskType(task.getTaskType())
                .title(task.getTitle())
                .dueDate(task.getDueDate())
                .completed(task.isCompleted())
                .build();
    }

    private User resolveUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserStats resolveUserStats(String email) {
        User user = resolveUser(email);
        return userStatsRepository.findByUser(user)
                .orElseGet(() -> userStatsRepository.save(
                        UserStats.builder()
                                .user(user)
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));
    }
}
