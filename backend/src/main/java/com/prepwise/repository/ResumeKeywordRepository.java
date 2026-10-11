package com.prepwise.repository;

import com.prepwise.entity.KeywordStatus;
import com.prepwise.entity.ResumeAnalysis;
import com.prepwise.entity.ResumeKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeKeywordRepository extends JpaRepository<ResumeKeyword, Long> {

    List<ResumeKeyword> findByResumeAnalysis(ResumeAnalysis resumeAnalysis);

    List<ResumeKeyword> findByResumeAnalysisAndStatus(ResumeAnalysis resumeAnalysis, KeywordStatus status);

    void deleteByResumeAnalysis(ResumeAnalysis resumeAnalysis);
}
