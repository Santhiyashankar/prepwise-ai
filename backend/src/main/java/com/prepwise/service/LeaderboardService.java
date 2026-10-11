
package com.prepwise.service;

import com.prepwise.dto.response.leaderboard.LeaderboardEntryResponse;
import com.prepwise.entity.InterviewSessionStatus;
import com.prepwise.repository.InterviewSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderboardService {

    private final InterviewSessionRepository interviewSessionRepository;

    public LeaderboardService(
            InterviewSessionRepository interviewSessionRepository
    ) {
        this.interviewSessionRepository = interviewSessionRepository;
    }

    @Transactional(readOnly = true)
    public List<LeaderboardEntryResponse> getLeaderboard() {

        List<Object[]> results =
                interviewSessionRepository.findLeaderboardStats(
                        InterviewSessionStatus.COMPLETED
                );

        List<LeaderboardEntryResponse> leaderboard = new ArrayList<>();

        int rank = 1;

        for (Object[] row : results) {
            Long userId = ((Number) row[0]).longValue();
            String fullName = (String) row[1];
            double averageScore = ((Number) row[2]).doubleValue();
            long completedInterviews = ((Number) row[3]).longValue();

            leaderboard.add(
                    LeaderboardEntryResponse.builder()
                            .rank(rank++)
                            .userId(userId)
                            .fullName(fullName)
                            .averageScore(
                                    Math.round(averageScore * 100.0) / 100.0
                            )
                            .completedInterviews(completedInterviews)
                            .build()
            );
        }

        return leaderboard;
    }
}