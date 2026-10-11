
package com.prepwise.repository;

import com.prepwise.entity.InterviewSession;
import com.prepwise.entity.InterviewSessionStatus;
import com.prepwise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {

    List<InterviewSession> findByUserOrderByStartedAtDesc(User user);

    Optional<InterviewSession> findByIdAndUser(Long id, User user);

    long countByUserAndStatus(User user, InterviewSessionStatus status);

    void deleteByUser(User user);

    @Query("""
        SELECT s.user.id,
               s.user.fullName,
               AVG(s.overallScore),
               COUNT(s.id)
        FROM InterviewSession s
        WHERE s.status = :status
          AND s.overallScore IS NOT NULL
        GROUP BY s.user.id, s.user.fullName
        ORDER BY AVG(s.overallScore) DESC, COUNT(s.id) DESC
        """)
    List<Object[]> findLeaderboardStats(
            @Param("status") InterviewSessionStatus status
    );
}