package com.prepwise.repository;

import com.prepwise.entity.GroupDiscussionEvaluation;
import com.prepwise.entity.GroupDiscussionSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupDiscussionEvaluationRepository extends JpaRepository<GroupDiscussionEvaluation, Long> {

    Optional<GroupDiscussionEvaluation> findBySession(GroupDiscussionSession session);

    void deleteBySession(GroupDiscussionSession session);
}
