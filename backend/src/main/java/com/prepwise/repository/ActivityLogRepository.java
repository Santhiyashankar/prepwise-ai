package com.prepwise.repository;

import com.prepwise.entity.ActivityLog;
import com.prepwise.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    List<ActivityLog> findByUserAndCreatedAtAfterOrderByCreatedAtAsc(User user, LocalDateTime after);

    void deleteByUser(User user);
}
