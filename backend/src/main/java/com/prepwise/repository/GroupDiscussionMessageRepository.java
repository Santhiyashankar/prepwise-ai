package com.prepwise.repository;

import com.prepwise.entity.GroupDiscussionMessage;
import com.prepwise.entity.GroupDiscussionSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupDiscussionMessageRepository extends JpaRepository<GroupDiscussionMessage, Long> {

    List<GroupDiscussionMessage> findBySessionOrderBySequenceNumberAsc(GroupDiscussionSession session);

    long countBySession(GroupDiscussionSession session);

    void deleteBySession(GroupDiscussionSession session);
}
