package com.prepwise.repository;

import com.prepwise.entity.GroupDiscussionSession;
import com.prepwise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupDiscussionSessionRepository extends JpaRepository<GroupDiscussionSession, Long> {

    List<GroupDiscussionSession> findByUserOrderByStartedAtDesc(User user);

    Optional<GroupDiscussionSession> findByIdAndUser(Long id, User user);

    void deleteByUser(User user);
}
