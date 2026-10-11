package com.prepwise.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepwise.dto.response.resume.KeywordAnalysisResponse;
import com.prepwise.dto.response.resume.KeywordItemResponse;
import com.prepwise.dto.response.resume.ResumeAnalysisResponse;
import com.prepwise.dto.response.resume.ResumeComparisonResponse;
import com.prepwise.dto.response.resume.ResumeFullResponse;
import com.prepwise.dto.response.resume.ResumeHistoryItemResponse;
import com.prepwise.dto.response.resume.ResumeResponse;
import com.prepwise.entity.FeedbackCategory;
import com.prepwise.entity.KeywordStatus;
import com.prepwise.entity.Resume;
import com.prepwise.entity.ResumeAnalysis;
import com.prepwise.entity.ResumeFeedback;
import com.prepwise.entity.ResumeFileType;
import com.prepwise.entity.ResumeHistory;
import com.prepwise.entity.ResumeKeyword;
import com.prepwise.entity.User;
import com.prepwise.entity.ActivityLog;
import com.prepwise.entity.ActivityType;
import com.prepwise.exception.ApiException;
import com.prepwise.exception.ResourceNotFoundException;
import com.prepwise.repository.ActivityLogRepository;
import com.prepwise.repository.ResumeAnalysisRepository;
import com.prepwise.repository.ResumeFeedbackRepository;
import com.prepwise.repository.ResumeHistoryRepository;
import com.prepwise.repository.ResumeKeywordRepository;
import com.prepwise.repository.ResumeRepository;
import com.prepwise.repository.UserRepository;
import com.prepwise.repository.UserStatsRepository;
import com.prepwise.service.AIService;
import com.prepwise.service.AtsScoreBreakdown;
import com.prepwise.service.AtsScoringService;
import com.prepwise.service.FileStorageService;
import com.prepwise.service.KeywordAnalysisResult;
import com.prepwise.service.KeywordAnalysisService;
import com.prepwise.service.ResumeParserService;
import com.prepwise.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeServiceImpl implements ResumeService {

    private static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024;
    private static final List<String> ALLOWED_EXTENSIONS = List.of("pdf", "docx");

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final ResumeFeedbackRepository resumeFeedbackRepository;
    private final ResumeKeywordRepository resumeKeywordRepository;
    private final ResumeHistoryRepository resumeHistoryRepository;
    private final ActivityLogRepository activityLogRepository;
    private final UserStatsRepository userStatsRepository;
    private final ResumeParserService resumeParserService;
    private final AtsScoringService atsScoringService;
    private final KeywordAnalysisService keywordAnalysisService;
    private final AIService aiService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper;

    @Override
    public ResumeResponse uploadResume(String email, MultipartFile file) {
        User user = resolveUser(email);
        validateFile(file);

        ResumeParserService.ParsedResume parsed = resumeParserService.parse(file);
        String extension = resolveExtension(file.getOriginalFilename());
        FileStorageService.StoredFile storedFile = fileStorageService.storeResume(user.getId(), file, extension);

        int nextVersion = (int) resumeRepository.countByUser(user) + 1;

        Resume resume = Resume.builder()
                .user(user)
                .fileName(file.getOriginalFilename())
                .storedFileName(storedFile.storedFileName)
                .filePath(storedFile.absolutePath)
                .fileType(".pdf".equalsIgnoreCase(extension) ? ResumeFileType.PDF : ResumeFileType.DOCX)
                .fileSizeBytes(file.getSize())
                .version(nextVersion)
                .extractedText(parsed.rawText)
                .fullName(parsed.fullName)
                .email(parsed.email)
                .phone(parsed.phone)
                .linkedinUrl(parsed.linkedinUrl)
                .githubUrl(parsed.githubUrl)
                .portfolioUrl(parsed.portfolioUrl)
                .skillsJson(toJson(parsed.skills))
                .educationJson(toJson(parsed.education))
                .experienceJson(toJson(parsed.experience))
                .projectsJson(toJson(parsed.projects))
                .certificationsJson(toJson(parsed.certifications))
                .languagesJson(toJson(parsed.languages))
                .achievementsJson(toJson(parsed.achievements))
                .build();

        Resume savedResume = resumeRepository.save(resume);

        activityLogRepository.save(ActivityLog.builder()
                .user(user)
                .activityType(ActivityType.RESUME_UPLOAD)
                .title("Resume uploaded: " + file.getOriginalFilename())
                .description("Version " + nextVersion)
                .build());

        return mapToResumeResponse(savedResume);
    }

    @Override
    public ResumeFullResponse analyzeResume(String email, Long resumeId) {
        return runAnalysis(email, resumeId);
    }

    @Override
    public ResumeFullResponse reanalyzeResume(String email, Long resumeId) {
        return runAnalysis(email, resumeId);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeFullResponse getLatestResume(String email) {
        User user = resolveUser(email);
        Resume resume = resumeRepository.findFirstByUserOrderByUploadedAtDesc(user)
                .orElseThrow(() -> new ResourceNotFoundException("No resume has been uploaded yet"));
        return buildFullResponse(resume);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeFullResponse getResumeById(String email, Long resumeId) {
        User user = resolveUser(email);
        Resume resume = resolveOwnedResume(user, resumeId);
        return buildFullResponse(resume);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeHistoryItemResponse> getHistory(String email) {
        User user = resolveUser(email);
        return resumeHistoryRepository.findByUserOrderByRecordedAtAsc(user).stream()
                .map(this::mapToHistoryItem)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteResume(String email, Long resumeId) {
        User user = resolveUser(email);
        Resume resume = resolveOwnedResume(user, resumeId);

        List<ResumeAnalysis> analyses = resumeAnalysisRepository.findByResumeOrderByAnalyzedAtDesc(resume);
        for (ResumeAnalysis analysis : analyses) {
            resumeFeedbackRepository.deleteByResumeAnalysis(analysis);
            resumeKeywordRepository.deleteByResumeAnalysis(analysis);
        }
        resumeAnalysisRepository.deleteByResume(resume);
        resumeHistoryRepository.deleteByResume(resume);

        deleteFileQuietly(resume.getFilePath());
        resumeRepository.delete(resume);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeComparisonResponse compareResumes(String email, Long firstId, Long secondId) {
        User user = resolveUser(email);
        Resume first = resolveOwnedResume(user, firstId);
        Resume second = resolveOwnedResume(user, secondId);

        return ResumeComparisonResponse.builder()
                .first(buildFullResponse(first))
                .second(buildFullResponse(second))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadResume(String email, Long resumeId) {
        User user = resolveUser(email);
        Resume resume = resolveOwnedResume(user, resumeId);
        try {
            return Files.readAllBytes(Paths.get(resume.getFilePath()));
        } catch (IOException ex) {
            throw new ApiException("Resume file could not be found on the server", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getResumeFileName(String email, Long resumeId) {
        User user = resolveUser(email);
        Resume resume = resolveOwnedResume(user, resumeId);
        return resume.getFileName();
    }

    private ResumeFullResponse runAnalysis(String email, Long resumeId) {
        User user = resolveUser(email);
        Resume resume = resolveOwnedResume(user, resumeId);

        KeywordAnalysisResult keywordResult = keywordAnalysisService.analyze(resume.getExtractedText());
        AtsScoreBreakdown atsBreakdown = atsScoringService.computeScore(
                toParsedResume(resume), keywordResult.skillCoveragePercentage);

        ResumeAnalysisResponse aiResult = aiService.analyzeResume(resume.getExtractedText());

        ResumeAnalysis analysis = ResumeAnalysis.builder()
                .resume(resume)
                .atsScore(atsBreakdown.overallScore)
                .aiScore(aiResult.getOverallRating())
                .overallRating(aiResult.getOverallRating())
                .keywordMatchPercentage(keywordResult.skillCoveragePercentage)
                .atsCriteriaJson(toScoreJson(atsBreakdown.criteriaScores))
                .professionalSummary(aiResult.getProfessionalSummary())
                .careerObjective(aiResult.getCareerObjective())
                .professionalHeadline(aiResult.getProfessionalHeadline())
                .build();

        ResumeAnalysis savedAnalysis = resumeAnalysisRepository.save(analysis);

        saveFeedback(savedAnalysis, FeedbackCategory.STRENGTH, aiResult.getStrengths());
        saveFeedback(savedAnalysis, FeedbackCategory.WEAKNESS, aiResult.getWeaknesses());
        saveFeedback(savedAnalysis, FeedbackCategory.MISSING_SECTION, aiResult.getMissingSections());
        saveFeedback(savedAnalysis, FeedbackCategory.GRAMMAR_SUGGESTION, aiResult.getGrammarSuggestions());
        saveFeedback(savedAnalysis, FeedbackCategory.FORMATTING_SUGGESTION, aiResult.getFormattingSuggestions());
        saveFeedback(savedAnalysis, FeedbackCategory.PROJECT_IMPROVEMENT, aiResult.getProjectImprovements());
        saveFeedback(savedAnalysis, FeedbackCategory.EXPERIENCE_IMPROVEMENT, aiResult.getExperienceImprovements());
        saveFeedback(savedAnalysis, FeedbackCategory.SKILL_SUGGESTION, aiResult.getSkillSuggestions());

        saveKeywords(savedAnalysis, keywordResult.matched, KeywordStatus.MATCHED);
        saveKeywords(savedAnalysis, keywordResult.weak, KeywordStatus.WEAK);
        saveKeywords(savedAnalysis, keywordResult.missing, KeywordStatus.MISSING);
        saveKeywords(savedAnalysis, keywordResult.recommended, KeywordStatus.RECOMMENDED);

        resumeHistoryRepository.save(ResumeHistory.builder()
                .user(user)
                .resume(resume)
                .fileName(resume.getFileName())
                .atsScore(atsBreakdown.overallScore)
                .aiScore(aiResult.getOverallRating())
                .keywordMatchPercentage(keywordResult.skillCoveragePercentage)
                .build());

        activityLogRepository.save(ActivityLog.builder()
                .user(user)
                .activityType(ActivityType.RESUME_UPLOAD)
                .title("Resume analyzed: " + resume.getFileName())
                .description("ATS score " + Math.round(atsBreakdown.overallScore) + "/100")
                .score(atsBreakdown.overallScore)
                .build());

        userStatsRepository.findByUser(user).ifPresent(stats -> {
            stats.setAtsResumeScore(atsBreakdown.overallScore);
            userStatsRepository.save(stats);
        });

        return buildFullResponse(resume, savedAnalysis);
    }

    private void saveFeedback(ResumeAnalysis analysis, FeedbackCategory category, List<String> items) {
        if (items == null || items.isEmpty()) return;
        List<ResumeFeedback> entities = items.stream()
                .filter(item -> item != null && !item.isBlank())
                .map(item -> ResumeFeedback.builder()
                        .resumeAnalysis(analysis)
                        .category(category)
                        .content(item)
                        .build())
                .collect(Collectors.toList());
        resumeFeedbackRepository.saveAll(entities);
    }

    private void saveKeywords(ResumeAnalysis analysis, List<String> keywords, KeywordStatus status) {
        if (keywords == null || keywords.isEmpty()) return;
        List<ResumeKeyword> entities = keywords.stream()
                .map(keyword -> ResumeKeyword.builder()
                        .resumeAnalysis(analysis)
                        .keyword(keyword)
                        .status(status)
                        .build())
                .collect(Collectors.toList());
        resumeKeywordRepository.saveAll(entities);
    }

    private ResumeFullResponse buildFullResponse(Resume resume) {
        ResumeAnalysis latestAnalysis = resumeAnalysisRepository
                .findFirstByResumeOrderByAnalyzedAtDesc(resume)
                .orElse(null);
        return buildFullResponse(resume, latestAnalysis);
    }

    private ResumeFullResponse buildFullResponse(Resume resume, ResumeAnalysis analysis) {
        ResumeResponse resumeResponse = mapToResumeResponse(resume);

        if (analysis == null) {
            return ResumeFullResponse.builder()
                    .resume(resumeResponse)
                    .analysis(null)
                    .keywordAnalysis(null)
                    .build();
        }

        List<ResumeKeyword> keywordEntities = resumeKeywordRepository.findByResumeAnalysis(analysis);

        List<String> matched = filterKeywordsByStatus(keywordEntities, KeywordStatus.MATCHED);
        List<String> weak = filterKeywordsByStatus(keywordEntities, KeywordStatus.WEAK);
        List<String> missing = filterKeywordsByStatus(keywordEntities, KeywordStatus.MISSING);
        List<String> recommended = filterKeywordsByStatus(keywordEntities, KeywordStatus.RECOMMENDED);

        List<KeywordItemResponse> keywordItems = keywordEntities.stream()
                .map(entity -> KeywordItemResponse.builder()
                        .keyword(entity.getKeyword())
                        .status(entity.getStatus())
                        .build())
                .collect(Collectors.toList());

        KeywordAnalysisResponse keywordAnalysisResponse = KeywordAnalysisResponse.builder()
                .matchedKeywords(matched)
                .missingKeywords(missing)
                .weakKeywords(weak)
                .recommendedKeywords(recommended)
                .skillCoveragePercentage(analysis.getKeywordMatchPercentage())
                .keywords(keywordItems)
                .build();

        ResumeAnalysisResponse analysisResponse = ResumeAnalysisResponse.builder()
                .id(analysis.getId())
                .resumeId(resume.getId())
                .atsScore(analysis.getAtsScore())
                .aiScore(analysis.getAiScore())
                .overallRating(analysis.getOverallRating())
                .keywordMatchPercentage(analysis.getKeywordMatchPercentage())
                .atsBreakdown(fromScoreJson(analysis.getAtsCriteriaJson()))
                .professionalSummary(analysis.getProfessionalSummary())
                .careerObjective(analysis.getCareerObjective())
                .professionalHeadline(analysis.getProfessionalHeadline())
                .strengths(feedbackByCategory(analysis, FeedbackCategory.STRENGTH))
                .weaknesses(feedbackByCategory(analysis, FeedbackCategory.WEAKNESS))
                .missingSections(feedbackByCategory(analysis, FeedbackCategory.MISSING_SECTION))
                .grammarSuggestions(feedbackByCategory(analysis, FeedbackCategory.GRAMMAR_SUGGESTION))
                .formattingSuggestions(feedbackByCategory(analysis, FeedbackCategory.FORMATTING_SUGGESTION))
                .projectImprovements(feedbackByCategory(analysis, FeedbackCategory.PROJECT_IMPROVEMENT))
                .experienceImprovements(feedbackByCategory(analysis, FeedbackCategory.EXPERIENCE_IMPROVEMENT))
                .skillSuggestions(feedbackByCategory(analysis, FeedbackCategory.SKILL_SUGGESTION))
                .analyzedAt(analysis.getAnalyzedAt())
                .build();

        return ResumeFullResponse.builder()
                .resume(resumeResponse)
                .analysis(analysisResponse)
                .keywordAnalysis(keywordAnalysisResponse)
                .build();
    }

    private List<String> filterKeywordsByStatus(List<ResumeKeyword> keywords, KeywordStatus status) {
        return keywords.stream()
                .filter(keyword -> keyword.getStatus() == status)
                .map(ResumeKeyword::getKeyword)
                .collect(Collectors.toList());
    }

    private List<String> feedbackByCategory(ResumeAnalysis analysis, FeedbackCategory category) {
        return resumeFeedbackRepository.findByResumeAnalysisAndCategory(analysis, category).stream()
                .map(ResumeFeedback::getContent)
                .collect(Collectors.toList());
    }

    private ResumeResponse mapToResumeResponse(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .fileName(resume.getFileName())
                .fileType(resume.getFileType())
                .fileSizeBytes(resume.getFileSizeBytes())
                .version(resume.getVersion())
                .fullName(resume.getFullName())
                .email(resume.getEmail())
                .phone(resume.getPhone())
                .linkedinUrl(resume.getLinkedinUrl())
                .githubUrl(resume.getGithubUrl())
                .portfolioUrl(resume.getPortfolioUrl())
                .skills(fromJson(resume.getSkillsJson()))
                .education(fromJson(resume.getEducationJson()))
                .experience(fromJson(resume.getExperienceJson()))
                .projects(fromJson(resume.getProjectsJson()))
                .certifications(fromJson(resume.getCertificationsJson()))
                .languages(fromJson(resume.getLanguagesJson()))
                .achievements(fromJson(resume.getAchievementsJson()))
                .uploadedAt(resume.getUploadedAt())
                .build();
    }

    private ResumeHistoryItemResponse mapToHistoryItem(ResumeHistory history) {
        return ResumeHistoryItemResponse.builder()
                .id(history.getId())
                .resumeId(history.getResume().getId())
                .fileName(history.getFileName())
                .atsScore(history.getAtsScore())
                .aiScore(history.getAiScore())
                .keywordMatchPercentage(history.getKeywordMatchPercentage())
                .recordedAt(history.getRecordedAt())
                .build();
    }

    private ResumeParserService.ParsedResume toParsedResume(Resume resume) {
        ResumeParserService.ParsedResume parsed = new ResumeParserService.ParsedResume();
        parsed.rawText = resume.getExtractedText();
        parsed.fullName = resume.getFullName();
        parsed.email = resume.getEmail();
        parsed.phone = resume.getPhone();
        parsed.linkedinUrl = resume.getLinkedinUrl();
        parsed.githubUrl = resume.getGithubUrl();
        parsed.portfolioUrl = resume.getPortfolioUrl();
        parsed.skills = fromJson(resume.getSkillsJson());
        parsed.education = fromJson(resume.getEducationJson());
        parsed.experience = fromJson(resume.getExperienceJson());
        parsed.projects = fromJson(resume.getProjectsJson());
        parsed.certifications = fromJson(resume.getCertificationsJson());
        parsed.languages = fromJson(resume.getLanguagesJson());
        parsed.achievements = fromJson(resume.getAchievementsJson());
        return parsed;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException("Please select a resume file to upload", HttpStatus.BAD_REQUEST);
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new ApiException("Resume file must be smaller than 10MB", HttpStatus.BAD_REQUEST);
        }
        String extension = resolveExtension(file.getOriginalFilename()).replace(".", "").toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ApiException("Only PDF and DOCX files are supported", HttpStatus.BAD_REQUEST);
        }
    }

    private String resolveExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
    }

    private void deleteFileQuietly(String filePath) {
        try {
            if (filePath != null) {
                Files.deleteIfExists(Path.of(filePath));
            }
        } catch (IOException ignored) {
            // Non-critical: orphaned file cleanup can be handled by a maintenance job.
        }
    }

    private String toJson(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list == null ? Collections.emptyList() : list);
        } catch (Exception ex) {
            return "[]";
        }
    }

    private String toScoreJson(java.util.Map<String, Double> map) {
        try {
            return objectMapper.writeValueAsString(map == null ? Collections.emptyMap() : map);
        } catch (Exception ex) {
            return "{}";
        }
    }

    private java.util.Map<String, Double> fromScoreJson(String json) {
        if (json == null || json.isBlank()) return new java.util.LinkedHashMap<>();
        try {
            return objectMapper.readValue(json, new TypeReference<java.util.LinkedHashMap<String, Double>>() {});
        } catch (Exception ex) {
            return new java.util.LinkedHashMap<>();
        }
    }

    private List<String> fromJson(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    private User resolveUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Resume resolveOwnedResume(User user, Long resumeId) {
        return resumeRepository.findByIdAndUser(resumeId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
    }
}
