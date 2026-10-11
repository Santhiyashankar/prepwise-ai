package com.prepwise.repository;

import com.prepwise.entity.User;
import com.prepwise.entity.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {

    Optional<UserStats> findByUser(User user);

    Optional<UserStats> findByUserId(Long userId);

    void deleteByUser(User user);
}
