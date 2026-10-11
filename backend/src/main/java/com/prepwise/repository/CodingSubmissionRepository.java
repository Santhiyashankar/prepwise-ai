package com.prepwise.repository;

import com.prepwise.entity.CodingProblem;
import com.prepwise.entity.CodingSubmission;
import com.prepwise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CodingSubmissionRepository extends JpaRepository<CodingSubmission, Long> {

    List<CodingSubmission> findByUserOrderBySubmittedAtDesc(User user);

    Optional<CodingSubmission> findByIdAndUser(Long id, User user);

    Optional<CodingSubmission> findFirstByUserAndProblemOrderBySubmittedAtDesc(User user, CodingProblem problem);

    long countByUserAndProblemAndScoreGreaterThanEqual(User user, CodingProblem problem, double score);

    void deleteByUser(User user);
}
