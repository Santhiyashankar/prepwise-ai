package com.prepwise.repository;

import com.prepwise.entity.CodingSubmission;
import com.prepwise.entity.CodingTestCaseResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodingTestCaseResultRepository extends JpaRepository<CodingTestCaseResult, Long> {

    List<CodingTestCaseResult> findBySubmission(CodingSubmission submission);

    void deleteBySubmission(CodingSubmission submission);
}
