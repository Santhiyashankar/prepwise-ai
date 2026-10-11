package com.prepwise.service;

import com.prepwise.dto.response.resume.ResumeComparisonResponse;
import com.prepwise.dto.response.resume.ResumeFullResponse;
import com.prepwise.dto.response.resume.ResumeHistoryItemResponse;
import com.prepwise.dto.response.resume.ResumeResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService {

    ResumeResponse uploadResume(String email, MultipartFile file);

    ResumeFullResponse analyzeResume(String email, Long resumeId);

    ResumeFullResponse reanalyzeResume(String email, Long resumeId);

    ResumeFullResponse getLatestResume(String email);

    ResumeFullResponse getResumeById(String email, Long resumeId);

    List<ResumeHistoryItemResponse> getHistory(String email);

    void deleteResume(String email, Long resumeId);

    ResumeComparisonResponse compareResumes(String email, Long firstId, Long secondId);

    byte[] downloadResume(String email, Long resumeId);

    String getResumeFileName(String email, Long resumeId);
}
