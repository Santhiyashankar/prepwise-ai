package com.prepwise.repository;

import com.prepwise.entity.InterviewEvaluation;
import com.prepwise.entity.InterviewQuestion;
import com.prepwise.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewEvaluationRepository extends JpaRepository<InterviewEvaluation, Long> {

    Optional<InterviewEvaluation> findByQuestion(InterviewQuestion question);

    @org.springframework.data.jpa.repository.Query(
            "SELECT e FROM InterviewEvaluation e WHERE e.question.session = :session ORDER BY e.question.questionNumber ASC")
    List<InterviewEvaluation> findBySessionOrderByQuestionNumber(InterviewSession session);

    void deleteByQuestion(InterviewQuestion question);
}
