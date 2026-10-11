package com.prepwise.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepwise.dto.request.coding.CodeExecutionRequest;
import com.prepwise.dto.request.coding.TimedAssessmentRequest;
import com.prepwise.dto.response.coding.CodingHistoryItemResponse;
import com.prepwise.dto.response.coding.CodingProblemDetailResponse;
import com.prepwise.dto.response.coding.CodingProblemSummaryResponse;
import com.prepwise.dto.response.coding.CodingReportResponse;
import com.prepwise.dto.response.coding.CodingSubmissionDetailResponse;
import com.prepwise.dto.response.coding.RunCodeResponse;
import com.prepwise.dto.response.coding.SubmitCodeResponse;
import com.prepwise.dto.response.coding.TestCaseRunResultResponse;
import com.prepwise.dto.response.coding.TestCaseSampleResponse;
import com.prepwise.dto.response.coding.TimedAssessmentResponse;
import com.prepwise.entity.ActivityLog;
import com.prepwise.entity.ActivityType;
import com.prepwise.entity.CodingCompany;
import com.prepwise.entity.CodingDifficulty;
import com.prepwise.entity.CodingProblem;
import com.prepwise.entity.CodingReport;
import com.prepwise.entity.CodingSubmission;
import com.prepwise.entity.CodingTestCase;
import com.prepwise.entity.CodingTestCaseResult;
import com.prepwise.entity.CodingTopic;
import com.prepwise.entity.SubmissionStatus;
import com.prepwise.entity.User;
import com.prepwise.entity.UserStats;
import com.prepwise.exception.ResourceNotFoundException;
import com.prepwise.repository.ActivityLogRepository;
import com.prepwise.repository.CodingProblemRepository;
import com.prepwise.repository.CodingReportRepository;
import com.prepwise.repository.CodingSubmissionRepository;
import com.prepwise.repository.CodingTestCaseRepository;
import com.prepwise.repository.CodingTestCaseResultRepository;
import com.prepwise.repository.UserRepository;
import com.prepwise.repository.UserStatsRepository;
import com.prepwise.service.AIService;
import com.prepwise.service.CodeExecutionService;
import com.prepwise.service.CodingReviewContext;
import com.prepwise.service.CodingService;
import com.prepwise.service.CompiledProgram;
import com.prepwise.service.ExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CodingServiceImpl implements CodingService {

    private final UserRepository userRepository;
    private final CodingProblemRepository problemRepository;
    private final CodingTestCaseRepository testCaseRepository;
    private final CodingSubmissionRepository submissionRepository;
    private final CodingTestCaseResultRepository testCaseResultRepository;
    private final CodingReportRepository reportRepository;
    private final ActivityLogRepository activityLogRepository;
    private final UserStatsRepository userStatsRepository;
    private final CodeExecutionService codeExecutionService;
    private final AIService aiService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CodingProblemSummaryResponse> listProblems(
            CodingDifficulty difficulty, CodingTopic topic, CodingCompany company) {
        return problemRepository.search(difficulty, topic, company).stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CodingProblemDetailResponse getProblem(String email, String slug) {
        CodingProblem problem = problemRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found"));
        return mapToDetail(resolveUser(email), problem);
    }

    @Override
    @Transactional(readOnly = true)
    public CodingProblemDetailResponse getDailyChallenge(String email) {
        List<CodingProblem> allProblems = problemRepository.findAll();
        if (allProblems.isEmpty()) {
            throw new ResourceNotFoundException("No coding problems are available yet");
        }
        int index = LocalDate.now().getDayOfYear() % allProblems.size();
        return mapToDetail(resolveUser(email), allProblems.get(index));
    }

    @Override
    @Transactional(readOnly = true)
    public TimedAssessmentResponse generateTimedAssessment(TimedAssessmentRequest request) {
        List<CodingProblem> candidates = problemRepository.findByDifficulty(request.getDifficulty());
        if (candidates.isEmpty()) {
            throw new ResourceNotFoundException("No problems available for this difficulty");
        }

        List<CodingProblem> shuffled = new ArrayList<>(candidates);
        Collections.shuffle(shuffled);
        List<CodingProblem> selected = shuffled.stream()
                .limit(Math.min(request.getProblemCount(), shuffled.size()))
                .toList();

        int totalTimeLimitMs = selected.stream().mapToInt(CodingProblem::getTimeLimitMs).sum();

        return TimedAssessmentResponse.builder()
                .problems(selected.stream().map(this::mapToSummary).collect(Collectors.toList()))
                .totalTimeLimitMs(totalTimeLimitMs)
                .build();
    }

    @Override
    public RunCodeResponse runCode(String email, CodeExecutionRequest request) {
        CodingProblem problem = resolveProblem(request.getProblemId());
        List<CodingTestCase> sampleTestCases = testCaseRepository.findByProblemAndSampleTrueOrderByOrderIndexAsc(problem);

        if (sampleTestCases.isEmpty()) {
            sampleTestCases = testCaseRepository.findByProblemOrderByOrderIndexAsc(problem).stream()
                    .limit(1)
                    .toList();
        }

        List<TestCaseRunResultResponse> results = new ArrayList<>();
        long maxExecutionTime = 0;
        String compileError = null;
        SubmissionStatus status = SubmissionStatus.COMPLETED;

        CompiledProgram program = codeExecutionService.compile(request.getLanguage(), request.getSourceCode());
        try {
            if (!program.compiled) {
                return RunCodeResponse.builder()
                        .status(SubmissionStatus.COMPILE_ERROR)
                        .compileErrorOutput(program.compileError)
                        .results(results)
                        .executionTimeMs(0L)
                        .build();
            }

            for (CodingTestCase testCase : sampleTestCases) {
                ExecutionResult execution = codeExecutionService.run(program, testCase.getInput(), problem.getTimeLimitMs());

                if (execution.timedOut) {
                    status = SubmissionStatus.TIMEOUT;
                }

                String actualOutput = execution.stdout.trim();
                boolean passed = !execution.timedOut && actualOutput.equals(testCase.getExpectedOutput().trim());
                maxExecutionTime = Math.max(maxExecutionTime, execution.executionTimeMs);

                results.add(TestCaseRunResultResponse.builder()
                        .input(testCase.getInput())
                        .expectedOutput(testCase.getExpectedOutput())
                        .actualOutput(execution.timedOut ? "(timed out)" : actualOutput)
                        .passed(passed)
                        .executionTimeMs(execution.executionTimeMs)
                        .build());
            }
        } finally {
            codeExecutionService.cleanup(program);
        }

        return RunCodeResponse.builder()
                .status(status)
                .compileErrorOutput(compileError)
                .results(results)
                .executionTimeMs(maxExecutionTime)
                .build();
    }

    @Override
    public SubmitCodeResponse submitCode(String email, CodeExecutionRequest request) {
        User user = resolveUser(email);
        CodingProblem problem = resolveProblem(request.getProblemId());
        List<CodingTestCase> testCases = testCaseRepository.findByProblemOrderByOrderIndexAsc(problem);

        if (testCases.isEmpty()) {
            throw new ResourceNotFoundException("This problem has no test cases configured");
        }

        CodingSubmission submission = CodingSubmission.builder()
                .user(user)
                .problem(problem)
                .language(request.getLanguage())
                .sourceCode(request.getSourceCode())
                .totalTestCases(testCases.size())
                .build();

        List<TestCaseRunResultResponse> resultResponses = new ArrayList<>();
        List<CodingTestCaseResult> resultEntities = new ArrayList<>();
        int passedCount = 0;
        long maxExecutionTime = 0;
        Long maxMemoryKb = null;
        String compileError = null;
        boolean compiled = true;
        boolean anyTimedOut = false;

        CompiledProgram program = codeExecutionService.compile(request.getLanguage(), request.getSourceCode());
        try {
            if (!program.compiled) {
                compiled = false;
                compileError = program.compileError;
            } else {
                for (CodingTestCase testCase : testCases) {
                    ExecutionResult execution =
                            codeExecutionService.run(program, testCase.getInput(), problem.getTimeLimitMs());

                    if (execution.timedOut) anyTimedOut = true;

                    String actualOutput = execution.stdout.trim();
                    boolean passed = !execution.timedOut && actualOutput.equals(testCase.getExpectedOutput().trim());
                    if (passed) passedCount++;

                    maxExecutionTime = Math.max(maxExecutionTime, execution.executionTimeMs);
                    if (execution.memoryUsageKb != null) {
                        maxMemoryKb = maxMemoryKb == null
                                ? execution.memoryUsageKb
                                : Math.max(maxMemoryKb, execution.memoryUsageKb);
                    }

                    resultResponses.add(TestCaseRunResultResponse.builder()
                            .input(testCase.getInput())
                            .expectedOutput(testCase.getExpectedOutput())
                            .actualOutput(execution.timedOut ? "(timed out)" : actualOutput)
                            .passed(passed)
                            .executionTimeMs(execution.executionTimeMs)
                            .build());

                    resultEntities.add(CodingTestCaseResult.builder()
                            .testCase(testCase)
                            .passed(passed)
                            .actualOutput(execution.timedOut ? "(timed out)" : actualOutput)
                            .executionTimeMs(execution.executionTimeMs)
                            .build());
                }
            }
        } finally {
            codeExecutionService.cleanup(program);
        }

        double score = compiled ? Math.round(((double) passedCount / testCases.size()) * 10000.0) / 100.0 : 0.0;

        submission.setStatus(!compiled ? SubmissionStatus.COMPILE_ERROR
                : anyTimedOut && passedCount == 0 ? SubmissionStatus.TIMEOUT
                : SubmissionStatus.COMPLETED);
        submission.setScore(score);
        submission.setPassedTestCases(passedCount);
        submission.setExecutionTimeMs(compiled ? maxExecutionTime : null);
        submission.setMemoryUsageKb(maxMemoryKb);
        submission.setCompileErrorOutput(compileError);

        CodingSubmission savedSubmission = submissionRepository.save(submission);

        for (CodingTestCaseResult resultEntity : resultEntities) {
            resultEntity.setSubmission(savedSubmission);
        }
        testCaseResultRepository.saveAll(resultEntities);

        updateProblemStatistics(problem, score);

        CodingReportResponse reportResponse = null;
        if (compiled) {
            CodingReviewContext context = new CodingReviewContext();
            context.problemTitle = problem.getTitle();
            context.problemDescription = problem.getDescription();
            context.difficulty = problem.getDifficulty().name();
            context.topic = problem.getTopic().name();
            context.company = problem.getCompany() != null ? problem.getCompany().name() : null;
            context.language = request.getLanguage().name();
            context.sourceCode = request.getSourceCode();
            context.passedTestCases = passedCount;
            context.totalTestCases = testCases.size();

            reportResponse = aiService.reviewCode(context);
            persistReport(savedSubmission, reportResponse);
        }

        updateUserStatsAndActivity(user, problem, score);

        return SubmitCodeResponse.builder()
                .submissionId(savedSubmission.getId())
                .problemId(problem.getId())
                .problemTitle(problem.getTitle())
                .language(request.getLanguage())
                .status(savedSubmission.getStatus())
                .score(score)
                .passedTestCases(passedCount)
                .totalTestCases(testCases.size())
                .executionTimeMs(savedSubmission.getExecutionTimeMs())
                .memoryUsageKb(savedSubmission.getMemoryUsageKb())
                .compileErrorOutput(compileError)
                .testCaseResults(resultResponses)
                .report(reportResponse)
                .submittedAt(savedSubmission.getSubmittedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingHistoryItemResponse> getHistory(String email) {
        User user = resolveUser(email);
        return submissionRepository.findByUserOrderBySubmittedAtDesc(user).stream()
                .map(this::mapToHistoryItem)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CodingSubmissionDetailResponse getSubmission(String email, Long submissionId) {
        User user = resolveUser(email);
        CodingSubmission submission = submissionRepository.findByIdAndUser(submissionId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));

        List<CodingTestCaseResult> testCaseResults = testCaseResultRepository.findBySubmission(submission);
        List<TestCaseRunResultResponse> results = testCaseResults.stream()
                .map(result -> TestCaseRunResultResponse.builder()
                        .input(result.getTestCase().getInput())
                        .expectedOutput(result.getTestCase().getExpectedOutput())
                        .actualOutput(result.getActualOutput())
                        .passed(result.isPassed())
                        .executionTimeMs(result.getExecutionTimeMs())
                        .build())
                .collect(Collectors.toList());

        CodingReportResponse reportResponse = reportRepository.findBySubmission(submission)
                .map(this::mapToReportResponse)
                .orElse(null);

        return CodingSubmissionDetailResponse.builder()
                .submissionId(submission.getId())
                .problemId(submission.getProblem().getId())
                .problemTitle(submission.getProblem().getTitle())
                .language(submission.getLanguage())
                .sourceCode(submission.getSourceCode())
                .status(submission.getStatus())
                .score(submission.getScore())
                .passedTestCases(submission.getPassedTestCases())
                .totalTestCases(submission.getTotalTestCases())
                .executionTimeMs(submission.getExecutionTimeMs())
                .memoryUsageKb(submission.getMemoryUsageKb())
                .compileErrorOutput(submission.getCompileErrorOutput())
                .testCaseResults(results)
                .report(reportResponse)
                .submittedAt(submission.getSubmittedAt())
                .build();
    }

    private void updateProblemStatistics(CodingProblem problem, double score) {
        problem.setTotalSubmissions(problem.getTotalSubmissions() + 1);
        if (score >= 100.0) {
            problem.setAcceptedSubmissions(problem.getAcceptedSubmissions() + 1);
        }
        double acceptanceRate = problem.getTotalSubmissions() == 0
                ? 0.0
                : Math.round(((double) problem.getAcceptedSubmissions() / problem.getTotalSubmissions()) * 10000.0) / 100.0;
        problem.setAcceptanceRate(acceptanceRate);
        problemRepository.save(problem);
    }

    private void persistReport(CodingSubmission submission, CodingReportResponse response) {
        CodingReport report = CodingReport.builder()
                .submission(submission)
                .overallScore(response.getOverallScore())
                .correctnessScore(response.getCorrectnessScore())
                .readabilityScore(response.getReadabilityScore())
                .bestPracticesScore(response.getBestPracticesScore())
                .edgeCaseScore(response.getEdgeCaseScore())
                .timeComplexity(response.getTimeComplexity())
                .spaceComplexity(response.getSpaceComplexity())
                .strengthsJson(toJson(response.getStrengths()))
                .weaknessesJson(toJson(response.getWeaknesses()))
                .optimizationSuggestionsJson(toJson(response.getOptimizationSuggestions()))
                .optimizedSolution(response.getOptimizedSolution())
                .build();
        reportRepository.save(report);
    }

    private void updateUserStatsAndActivity(User user, CodingProblem problem, double score) {
        activityLogRepository.save(ActivityLog.builder()
                .user(user)
                .activityType(ActivityType.CODING_SUBMISSION)
                .title("Solved: " + problem.getTitle())
                .description(problem.getDifficulty() + " \u00b7 " + problem.getTopic())
                .score(score)
                .build());

        UserStats stats = userStatsRepository.findByUser(user)
                .orElseGet(() -> userStatsRepository.save(UserStats.builder().user(user).updatedAt(LocalDateTime.now()).build()));

        if (score >= 100.0) {
            long timesSolvedAtFullScore =
                    submissionRepository.countByUserAndProblemAndScoreGreaterThanEqual(user, problem, 100.0);
            if (timesSolvedAtFullScore <= 1) {
                stats.setCodingProblemsSolved(stats.getCodingProblemsSolved() + 1);
            }
        }
        stats.setCurrentXp(stats.getCurrentXp() + (int) Math.round(score * 0.5));
        stats.setCurrentLevel(1 + (stats.getCurrentXp() / 500));

        LocalDate today = LocalDate.now();
        LocalDate lastActivity = stats.getLastActivityDate();
        if (lastActivity == null || ChronoUnit.DAYS.between(lastActivity, today) > 1) {
            stats.setDailyStreak(1);
        } else if (ChronoUnit.DAYS.between(lastActivity, today) == 1) {
            stats.setDailyStreak(stats.getDailyStreak() + 1);
        }
        stats.setLastActivityDate(today);

        userStatsRepository.save(stats);
    }

    private CodingProblemSummaryResponse mapToSummary(CodingProblem problem) {
        return CodingProblemSummaryResponse.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .slug(problem.getSlug())
                .difficulty(problem.getDifficulty())
                .topic(problem.getTopic())
                .company(problem.getCompany())
                .acceptanceRate(problem.getAcceptanceRate())
                .timeLimitMs(problem.getTimeLimitMs())
                .build();
    }

    private CodingProblemDetailResponse mapToDetail(User user, CodingProblem problem) {
        List<CodingTestCase> sampleTestCases = testCaseRepository.findByProblemAndSampleTrueOrderByOrderIndexAsc(problem);

        Map<String, String> starterCode = new LinkedHashMap<>();
        starterCode.put("JAVA", problem.getStarterCodeJava());
        starterCode.put("PYTHON", problem.getStarterCodePython());
        starterCode.put("CPP", problem.getStarterCodeCpp());
        starterCode.put("JAVASCRIPT", problem.getStarterCodeJavascript());

        Double lastScore = submissionRepository.findFirstByUserAndProblemOrderBySubmittedAtDesc(user, problem)
                .map(CodingSubmission::getScore)
                .orElse(null);

        return CodingProblemDetailResponse.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .slug(problem.getSlug())
                .description(problem.getDescription())
                .difficulty(problem.getDifficulty())
                .topic(problem.getTopic())
                .company(problem.getCompany())
                .timeLimitMs(problem.getTimeLimitMs())
                .memoryLimitMb(problem.getMemoryLimitMb())
                .acceptanceRate(problem.getAcceptanceRate())
                .starterCode(starterCode)
                .sampleTestCases(sampleTestCases.stream()
                        .map(testCase -> TestCaseSampleResponse.builder()
                                .input(testCase.getInput())
                                .expectedOutput(testCase.getExpectedOutput())
                                .build())
                        .collect(Collectors.toList()))
                .lastScore(lastScore)
                .build();
    }

    private CodingHistoryItemResponse mapToHistoryItem(CodingSubmission submission) {
        return CodingHistoryItemResponse.builder()
                .submissionId(submission.getId())
                .problemId(submission.getProblem().getId())
                .problemTitle(submission.getProblem().getTitle())
                .problemSlug(submission.getProblem().getSlug())
                .language(submission.getLanguage())
                .status(submission.getStatus())
                .score(submission.getScore())
                .submittedAt(submission.getSubmittedAt())
                .build();
    }

    private CodingReportResponse mapToReportResponse(CodingReport report) {
        return CodingReportResponse.builder()
                .overallScore(report.getOverallScore())
                .correctnessScore(report.getCorrectnessScore())
                .readabilityScore(report.getReadabilityScore())
                .bestPracticesScore(report.getBestPracticesScore())
                .edgeCaseScore(report.getEdgeCaseScore())
                .timeComplexity(report.getTimeComplexity())
                .spaceComplexity(report.getSpaceComplexity())
                .strengths(fromJson(report.getStrengthsJson()))
                .weaknesses(fromJson(report.getWeaknessesJson()))
                .optimizationSuggestions(fromJson(report.getOptimizationSuggestionsJson()))
                .optimizedSolution(report.getOptimizedSolution())
                .build();
    }

    private String toJson(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list == null ? Collections.emptyList() : list);
        } catch (Exception ex) {
            return "[]";
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

    private CodingProblem resolveProblem(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found"));
    }
}
