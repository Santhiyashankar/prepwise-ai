package com.prepwise.repository;

import com.prepwise.entity.CodingProblem;
import com.prepwise.entity.CodingTestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodingTestCaseRepository extends JpaRepository<CodingTestCase, Long> {

    List<CodingTestCase> findByProblemOrderByOrderIndexAsc(CodingProblem problem);

    List<CodingTestCase> findByProblemAndSampleTrueOrderByOrderIndexAsc(CodingProblem problem);
}
