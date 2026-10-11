package com.prepwise.repository;

import com.prepwise.entity.InterviewReport;
import com.prepwise.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewReportRepository extends JpaRepository<InterviewReport, Long> {

    Optional<InterviewReport> findBySession(InterviewSession session);

    void deleteBySession(InterviewSession session);
}
