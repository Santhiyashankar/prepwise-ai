
package com.prepwise.dto.response.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntryResponse {

    private int rank;

    private Long userId;

    private String fullName;

    private double averageScore;

    private long completedInterviews;
}