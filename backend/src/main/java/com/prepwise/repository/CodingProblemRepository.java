package com.prepwise.repository;

import com.prepwise.entity.CodingCompany;
import com.prepwise.entity.CodingDifficulty;
import com.prepwise.entity.CodingProblem;
import com.prepwise.entity.CodingTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CodingProblemRepository extends JpaRepository<CodingProblem, Long> {

    Optional<CodingProblem> findBySlug(String slug);

    @Query("""
            SELECT p FROM CodingProblem p
            WHERE (:difficulty IS NULL OR p.difficulty = :difficulty)
            AND (:topic IS NULL OR p.topic = :topic)
            AND (:company IS NULL OR p.company = :company)
            ORDER BY p.id ASC
            """)
    List<CodingProblem> search(CodingDifficulty difficulty, CodingTopic topic, CodingCompany company);

    List<CodingProblem> findByCompany(CodingCompany company);

    List<CodingProblem> findByTopic(CodingTopic topic);

    List<CodingProblem> findByDifficulty(CodingDifficulty difficulty);

    boolean existsBySlug(String slug);

}
