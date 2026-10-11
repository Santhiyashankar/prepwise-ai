package com.prepwise.repository;

import com.prepwise.entity.GroupDiscussionParticipant;
import com.prepwise.entity.GroupDiscussionSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupDiscussionParticipantRepository extends JpaRepository<GroupDiscussionParticipant, Long> {

    List<GroupDiscussionParticipant> findBySessionOrderByIdAsc(GroupDiscussionSession session);

    void deleteBySession(GroupDiscussionSession session);
}
