package com.prepwise.repository;

import com.prepwise.entity.CodingReport;
import com.prepwise.entity.CodingSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodingReportRepository extends JpaRepository<CodingReport, Long> {

    Optional<CodingReport> findBySubmission(CodingSubmission submission);

    void deleteBySubmission(CodingSubmission submission);
}
