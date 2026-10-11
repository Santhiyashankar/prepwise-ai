package com.prepwise.repository;

import com.prepwise.entity.Resume;
import com.prepwise.entity.ResumeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeAnalysisRepository extends JpaRepository<ResumeAnalysis, Long> {

    Optional<ResumeAnalysis> findFirstByResumeOrderByAnalyzedAtDesc(Resume resume);

    List<ResumeAnalysis> findByResumeOrderByAnalyzedAtDesc(Resume resume);

    void deleteByResume(Resume resume);
}
