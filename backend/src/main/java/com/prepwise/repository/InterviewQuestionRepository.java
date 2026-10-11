package com.prepwise.repository;

import com.prepwise.entity.InterviewQuestion;
import com.prepwise.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findBySessionOrderByQuestionNumberAsc(InterviewSession session);

    Optional<InterviewQuestion> findBySessionAndQuestionNumber(InterviewSession session, int questionNumber);

    void deleteBySession(InterviewSession session);
}
