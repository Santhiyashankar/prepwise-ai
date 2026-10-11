package com.prepwise.repository;

import com.prepwise.entity.FeedbackCategory;
import com.prepwise.entity.ResumeAnalysis;
import com.prepwise.entity.ResumeFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeFeedbackRepository extends JpaRepository<ResumeFeedback, Long> {

    List<ResumeFeedback> findByResumeAnalysisAndCategory(ResumeAnalysis resumeAnalysis, FeedbackCategory category);

    List<ResumeFeedback> findByResumeAnalysis(ResumeAnalysis resumeAnalysis);

    void deleteByResumeAnalysis(ResumeAnalysis resumeAnalysis);
}
