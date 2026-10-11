package com.prepwise.repository;

import com.prepwise.entity.InterviewAnswer;
import com.prepwise.entity.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {

    Optional<InterviewAnswer> findByQuestion(InterviewQuestion question);

    void deleteByQuestion(InterviewQuestion question);
}
