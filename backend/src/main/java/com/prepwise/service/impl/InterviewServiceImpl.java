package com.prepwise.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepwise.dto.request.interview.StartInterviewRequest;
import com.prepwise.dto.request.interview.SubmitAnswerRequest;
import com.prepwise.dto.response.interview.ImprovementPlanResponse;
import com.prepwise.dto.response.interview.InterviewEvaluationResponse;
import com.prepwise.dto.response.interview.InterviewHistoryItemResponse;
import com.prepwise.dto.response.interview.InterviewQuestionResponse;
import com.prepwise.dto.response.interview.InterviewReportResponse;
import com.prepwise.dto.response.interview.InterviewSessionResponse;
import com.prepwise.dto.response.interview.QuestionFeedbackResponse;
import com.prepwise.dto.response.interview.SubmitAnswerResponse;
import com.prepwise.entity.ActivityLog;
import com.prepwise.entity.ActivityType;
import com.prepwise.entity.InterviewAnswer;
import com.prepwise.entity.InterviewEvaluation;
import com.prepwise.entity.InterviewQuestion;
import com.prepwise.entity.InterviewReport;
import com.prepwise.entity.InterviewSession;
import com.prepwise.entity.InterviewSessionStatus;
import com.prepwise.entity.User;
import com.prepwise.entity.UserStats;
import com.prepwise.exception.ResourceNotFoundException;
import com.prepwise.repository.ActivityLogRepository;
import com.prepwise.repository.InterviewAnswerRepository;
import com.prepwise.repository.InterviewEvaluationRepository;
import com.prepwise.repository.InterviewQuestionRepository;
import com.prepwise.repository.InterviewReportRepository;
import com.prepwise.repository.InterviewSessionRepository;
import com.prepwise.repository.UserRepository;
import com.prepwise.repository.UserStatsRepository;
import com.prepwise.service.AIService;
import com.prepwise.service.InterviewContext;
import com.prepwise.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final UserRepository userRepository;
    private final InterviewSessionRepository sessionRepository;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewAnswerRepository answerRepository;
    private final InterviewEvaluationRepository evaluationRepository;
    private final InterviewReportRepository reportRepository;
    private final ActivityLogRepository activityLogRepository;
    private final UserStatsRepository userStatsRepository;
    private final AIService aiService;
    private final ObjectMapper objectMapper;

    @Override
    public InterviewSessionResponse startInterview(String email, StartInterviewRequest request) {
        User user = resolveUser(email);

        InterviewSession session = InterviewSession.builder()
                .user(user)
                .role(request.getRole())
                .difficulty(request.getDifficulty())
                .experience(request.getExperience())
                .style(request.getStyle())
                .totalQuestions(request.getQuestionCount())
                .currentQuestionNumber(1)
                .status(InterviewSessionStatus.IN_PROGRESS)
                .build();
        InterviewSession savedSession = sessionRepository.save(session);

        InterviewContext context = buildContext(savedSession, Collections.emptyList());
        String questionText = aiService.generateNextInterviewQuestion(context);

        InterviewQuestion question = questionRepository.save(InterviewQuestion.builder()
                .session(savedSession)
                .questionNumber(1)
                .questionText(questionText)
                .build());

        return InterviewSessionResponse.builder()
                .id(savedSession.getId())
                .role(savedSession.getRole())
                .difficulty(savedSession.getDifficulty())
                .experience(savedSession.getExperience())
                .style(savedSession.getStyle())
                .totalQuestions(savedSession.getTotalQuestions())
                .currentQuestionNumber(savedSession.getCurrentQuestionNumber())
                .status(savedSession.getStatus())
                .startedAt(savedSession.getStartedAt())
                .firstQuestion(mapToQuestionResponse(question, savedSession.getTotalQuestions()))
                .build();
    }

    @Override
    public SubmitAnswerResponse submitAnswer(
            String email, Long sessionId, int questionNumber, SubmitAnswerRequest request) {
        User user = resolveUser(email);
        InterviewSession session = resolveOwnedSession(user, sessionId);

        InterviewQuestion question = questionRepository.findBySessionAndQuestionNumber(session, questionNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        InterviewAnswer answer = InterviewAnswer.builder()
                .question(question)
                .answerText(request.getAnswerText())
                .skipped(request.isSkipped())
                .timeTakenSeconds(request.getTimeTakenSeconds())
                .build();
        answerRepository.save(answer);

        List<InterviewContext.QAPair> history = buildHistory(session);

        InterviewEvaluationResponse evaluationResult;
        if (request.isSkipped() || request.getAnswerText() == null || request.getAnswerText().isBlank()) {
            evaluationResult = InterviewEvaluationResponse.builder()
                    .score(0)
                    .technicalKnowledge(0)
                    .communication(0)
                    .confidence(0)
                    .problemSolving(0)
                    .timeManagement(0)
                    .strengths(Collections.emptyList())
                    .mistakes(List.of("This question was skipped."))
                    .idealAnswer("")
                    .followUpQuestion("")
                    .build();
        } else {
            InterviewContext context = buildContext(session, history);
            evaluationResult = aiService.evaluateInterviewAnswer(context, question.getQuestionText(), request.getAnswerText());
        }

        InterviewEvaluation evaluation = InterviewEvaluation.builder()
                .question(question)
                .score(evaluationResult.getScore())
                .technicalKnowledge(evaluationResult.getTechnicalKnowledge())
                .communication(evaluationResult.getCommunication())
                .confidence(evaluationResult.getConfidence())
                .problemSolving(evaluationResult.getProblemSolving())
                .timeManagement(evaluationResult.getTimeManagement())
                .strengthsJson(toJson(evaluationResult.getStrengths()))
                .mistakesJson(toJson(evaluationResult.getMistakes()))
                .idealAnswer(evaluationResult.getIdealAnswer())
                .followUpQuestion(evaluationResult.getFollowUpQuestion())
                .build();
        evaluationRepository.save(evaluation);

        boolean isLastQuestion = questionNumber >= session.getTotalQuestions();

        if (isLastQuestion) {
            completeInterview(session);
            return SubmitAnswerResponse.builder()
                    .evaluation(evaluationResult)
                    .nextQuestion(null)
                    .sessionStatus(InterviewSessionStatus.COMPLETED)
                    .interviewComplete(true)
                    .build();
        }

        history.add(new InterviewContext.QAPair(
                question.getQuestionText(),
                request.isSkipped() ? "(skipped)" : request.getAnswerText(),
                evaluationResult.getScore()
        ));

        int nextQuestionNumber = questionNumber + 1;
        InterviewContext nextContext = buildContext(session, history);
        nextContext.questionNumber = nextQuestionNumber;
        String nextQuestionText = aiService.generateNextInterviewQuestion(nextContext);

        InterviewQuestion nextQuestion = questionRepository.save(InterviewQuestion.builder()
                .session(session)
                .questionNumber(nextQuestionNumber)
                .questionText(nextQuestionText)
                .build());

        session.setCurrentQuestionNumber(nextQuestionNumber);
        sessionRepository.save(session);

        return SubmitAnswerResponse.builder()
                .evaluation(evaluationResult)
                .nextQuestion(mapToQuestionResponse(nextQuestion, session.getTotalQuestions()))
                .sessionStatus(InterviewSessionStatus.IN_PROGRESS)
                .interviewComplete(false)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewSessionResponse getSession(String email, Long sessionId) {
        User user = resolveUser(email);
        InterviewSession session = resolveOwnedSession(user, sessionId);

        InterviewQuestion currentQuestion = questionRepository
                .findBySessionAndQuestionNumber(session, session.getCurrentQuestionNumber())
                .orElse(null);

        return InterviewSessionResponse.builder()
                .id(session.getId())
                .role(session.getRole())
                .difficulty(session.getDifficulty())
                .experience(session.getExperience())
                .style(session.getStyle())
                .totalQuestions(session.getTotalQuestions())
                .currentQuestionNumber(session.getCurrentQuestionNumber())
                .status(session.getStatus())
                .overallScore(session.getOverallScore())
                .startedAt(session.getStartedAt())
                .completedAt(session.getCompletedAt())
                .firstQuestion(currentQuestion != null
                        ? mapToQuestionResponse(currentQuestion, session.getTotalQuestions())
                        : null)
                .build();
    }

    @Override
    public InterviewReportResponse getReport(String email, Long sessionId) {
        User user = resolveUser(email);
        InterviewSession session = resolveOwnedSession(user, sessionId);

        List<InterviewQuestion> questions = questionRepository.findBySessionOrderByQuestionNumberAsc(session);

        List<QuestionFeedbackResponse> feedback = new ArrayList<>();
        for (InterviewQuestion question : questions) {
            InterviewAnswer answer = answerRepository.findByQuestion(question).orElse(null);
            InterviewEvaluation evaluation = evaluationRepository.findByQuestion(question).orElse(null);

            feedback.add(QuestionFeedbackResponse.builder()
                    .questionNumber(question.getQuestionNumber())
                    .questionText(question.getQuestionText())
                    .answerText(answer != null ? answer.getAnswerText() : null)
                    .skipped(answer != null && answer.isSkipped())
                    .score(evaluation != null ? evaluation.getScore() : 0)
                    .strengths(evaluation != null ? fromJson(evaluation.getStrengthsJson()) : Collections.emptyList())
                    .mistakes(evaluation != null ? fromJson(evaluation.getMistakesJson()) : Collections.emptyList())
                    .idealAnswer(evaluation != null ? evaluation.getIdealAnswer() : null)
                    .build());
        }

        InterviewReport report = reportRepository.findBySession(session)
                .orElseGet(() -> generateReport(session, questions));

        long durationSeconds = session.getCompletedAt() != null
                ? Duration.between(session.getStartedAt(), session.getCompletedAt()).getSeconds()
                : 0;

        ImprovementPlanResponse improvementPlan = fromImprovementPlanJson(report.getImprovementPlanJson());
        List<String> badges = fromJson(report.getBadgesJson());

        return InterviewReportResponse.builder()
                .sessionId(session.getId())
                .role(session.getRole())
                .style(session.getStyle())
                .difficulty(session.getDifficulty())
                .overallScore(nullToZero(session.getOverallScore()))
                .technicalKnowledge(nullToZero(session.getTechnicalKnowledgeScore()))
                .communication(nullToZero(session.getCommunicationScore()))
                .confidence(nullToZero(session.getConfidenceScore()))
                .problemSolving(nullToZero(session.getProblemSolvingScore()))
                .timeManagement(nullToZero(session.getTimeManagementScore()))
                .durationSeconds(durationSeconds)
                .summary(report.getSummary())
                .questionFeedback(feedback)
                .improvementPlan(improvementPlan)
                .badges(badges)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewHistoryItemResponse> getHistory(String email) {
        User user = resolveUser(email);
        return sessionRepository.findByUserOrderByStartedAtDesc(user).stream()
                .map(this::mapToHistoryItem)
                .collect(Collectors.toList());
    }

    @Override
    public InterviewSessionResponse retryInterview(String email, Long sessionId) {
        User user = resolveUser(email);
        InterviewSession originalSession = resolveOwnedSession(user, sessionId);

        StartInterviewRequest request = new StartInterviewRequest(
                originalSession.getRole(),
                originalSession.getDifficulty(),
                originalSession.getExperience(),
                originalSession.getStyle(),
                originalSession.getTotalQuestions()
        );

        return startInterview(email, request);
    }

    @Override
    public InterviewReportResponse finishInterviewEarly(String email, Long sessionId) {
        User user = resolveUser(email);
        InterviewSession session = resolveOwnedSession(user, sessionId);

        if (session.getStatus() == InterviewSessionStatus.IN_PROGRESS) {
            List<InterviewEvaluation> evaluations = evaluationRepository.findBySessionOrderByQuestionNumber(session);
            if (evaluations.isEmpty()) {
                session.setStatus(InterviewSessionStatus.ABANDONED);
                session.setCompletedAt(LocalDateTime.now());
                sessionRepository.save(session);
            } else {
                completeInterview(session);
            }
        }

        return getReport(email, sessionId);
    }

    @Override
    public void deleteSession(String email, Long sessionId) {
        User user = resolveUser(email);
        InterviewSession session = resolveOwnedSession(user, sessionId);

        List<InterviewQuestion> questions = questionRepository.findBySessionOrderByQuestionNumberAsc(session);
        for (InterviewQuestion question : questions) {
            evaluationRepository.deleteByQuestion(question);
            answerRepository.deleteByQuestion(question);
        }
        questionRepository.deleteBySession(session);
        reportRepository.findBySession(session).ifPresent(reportRepository::delete);
        sessionRepository.delete(session);
    }

    private void completeInterview(InterviewSession session) {
        List<InterviewQuestion> questions = questionRepository.findBySessionOrderByQuestionNumberAsc(session);
        List<InterviewEvaluation> evaluations = evaluationRepository.findBySessionOrderByQuestionNumber(session);

        double overallScore = average(evaluations.stream().map(InterviewEvaluation::getScore).toList());
        double technical = average(evaluations.stream().map(InterviewEvaluation::getTechnicalKnowledge).toList());
        double communication = average(evaluations.stream().map(InterviewEvaluation::getCommunication).toList());
        double confidence = average(evaluations.stream().map(InterviewEvaluation::getConfidence).toList());
        double problemSolving = average(evaluations.stream().map(InterviewEvaluation::getProblemSolving).toList());
        double timeManagement = average(evaluations.stream().map(InterviewEvaluation::getTimeManagement).toList());

        session.setStatus(InterviewSessionStatus.COMPLETED);
        session.setCompletedAt(LocalDateTime.now());
        session.setOverallScore(overallScore);
        session.setTechnicalKnowledgeScore(technical);
        session.setCommunicationScore(communication);
        session.setConfidenceScore(confidence);
        session.setProblemSolvingScore(problemSolving);
        session.setTimeManagementScore(timeManagement);
        sessionRepository.save(session);

        generateReport(session, questions);
        updateUserStatsAndActivity(session, overallScore);
    }

    private InterviewReport generateReport(InterviewSession session, List<InterviewQuestion> questions) {
        InterviewContext context = buildContext(session, buildHistory(session));

        ImprovementPlanResponse improvementPlan = aiService.generateInterviewImprovementPlan(
                context, nullToZero(session.getOverallScore()));

        List<String> badges = computeBadges(session);

        String summary = "Completed a " + session.getTotalQuestions() + "-question " + session.getStyle()
                + " interview for " + session.getRole() + " at " + session.getDifficulty()
                + " difficulty, scoring " + Math.round(nullToZero(session.getOverallScore())) + "/100 overall.";

        InterviewReport report = InterviewReport.builder()
                .session(session)
                .summary(summary)
                .improvementPlanJson(toImprovementPlanJson(improvementPlan))
                .badgesJson(toJson(badges))
                .build();

        return reportRepository.save(report);
    }

    private List<String> computeBadges(InterviewSession session) {
        List<String> badges = new ArrayList<>();
        long completedCount = sessionRepository.countByUserAndStatus(session.getUser(), InterviewSessionStatus.COMPLETED);

        if (completedCount <= 1) badges.add("First Interview");
        if (completedCount >= 5) badges.add("5 Interviews Completed");
        if (completedCount >= 20) badges.add("20 Interviews Completed");
        if (nullToZero(session.getOverallScore()) >= 90) badges.add("Perfect Performance");
        if (nullToZero(session.getOverallScore()) >= 70) badges.add("Consistent Performer");
        return badges;
    }

    private void updateUserStatsAndActivity(InterviewSession session, double overallScore) {
        User user = session.getUser();

        activityLogRepository.save(ActivityLog.builder()
                .user(user)
                .activityType(ActivityType.INTERVIEW)
                .title(session.getRole() + " " + session.getStyle() + " interview completed")
                .description(session.getTotalQuestions() + " questions \u00b7 " + session.getDifficulty() + " difficulty")
                .score(overallScore)
                .build());

        UserStats stats = userStatsRepository.findByUser(user)
                .orElseGet(() -> userStatsRepository.save(UserStats.builder().user(user).updatedAt(LocalDateTime.now()).build()));

        double newAverage = stats.getTotalInterviews() == 0
                ? overallScore
                : ((stats.getAverageScore() * stats.getTotalInterviews()) + overallScore) / (stats.getTotalInterviews() + 1);

        stats.setTotalInterviews(stats.getTotalInterviews() + 1);
        stats.setAverageScore(Math.round(newAverage * 100.0) / 100.0);
        stats.setCurrentXp(stats.getCurrentXp() + (int) Math.round(overallScore));
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

    private InterviewContext buildContext(InterviewSession session, List<InterviewContext.QAPair> history) {
        InterviewContext context = new InterviewContext();
        context.role = session.getRole().name();
        context.difficulty = session.getDifficulty().name();
        context.experience = session.getExperience().name();
        context.style = session.getStyle().name();
        context.questionNumber = session.getCurrentQuestionNumber();
        context.totalQuestions = session.getTotalQuestions();
        context.history = history;
        return context;
    }

    private List<InterviewContext.QAPair> buildHistory(InterviewSession session) {
        List<InterviewQuestion> questions = questionRepository.findBySessionOrderByQuestionNumberAsc(session);
        List<InterviewContext.QAPair> history = new ArrayList<>();
        for (InterviewQuestion question : questions) {
            InterviewAnswer answer = answerRepository.findByQuestion(question).orElse(null);
            InterviewEvaluation evaluation = evaluationRepository.findByQuestion(question).orElse(null);
            if (answer != null && evaluation != null) {
                history.add(new InterviewContext.QAPair(
                        question.getQuestionText(),
                        answer.isSkipped() ? "(skipped)" : answer.getAnswerText(),
                        evaluation.getScore()
                ));
            }
        }
        return history;
    }

    private InterviewQuestionResponse mapToQuestionResponse(InterviewQuestion question, int totalQuestions) {
        return InterviewQuestionResponse.builder()
                .id(question.getId())
                .questionNumber(question.getQuestionNumber())
                .totalQuestions(totalQuestions)
                .questionText(question.getQuestionText())
                .build();
    }

    private InterviewHistoryItemResponse mapToHistoryItem(InterviewSession session) {
        long durationSeconds = session.getCompletedAt() != null
                ? Duration.between(session.getStartedAt(), session.getCompletedAt()).getSeconds()
                : 0;

        return InterviewHistoryItemResponse.builder()
                .sessionId(session.getId())
                .role(session.getRole())
                .style(session.getStyle())
                .difficulty(session.getDifficulty())
                .overallScore(session.getOverallScore())
                .totalQuestions(session.getTotalQuestions())
                .durationSeconds(durationSeconds)
                .status(session.getStatus())
                .startedAt(session.getStartedAt())
                .completedAt(session.getCompletedAt())
                .build();
    }

    private double average(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }

        return Math.round(
                values.stream()
                        .filter(value -> value != null)
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0) * 100.0
        ) / 100.0;
    }

    private double nullToZero(Double value) {
        return value == null ? 0.0 : value;
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

    private String toImprovementPlanJson(ImprovementPlanResponse plan) {
        try {
            return objectMapper.writeValueAsString(plan);
        } catch (Exception ex) {
            return "{}";
        }
    }

    private ImprovementPlanResponse fromImprovementPlanJson(String json) {
        if (json == null || json.isBlank()) {
            return ImprovementPlanResponse.builder()
                    .dailyPlan(Collections.emptyList())
                    .recommendedTopics(Collections.emptyList())
                    .projectsToRevise(Collections.emptyList())
                    .resumeImprovements(Collections.emptyList())
                    .build();
        }
        try {
            return objectMapper.readValue(json, ImprovementPlanResponse.class);
        } catch (Exception ex) {
            return ImprovementPlanResponse.builder()
                    .dailyPlan(Collections.emptyList())
                    .recommendedTopics(Collections.emptyList())
                    .projectsToRevise(Collections.emptyList())
                    .resumeImprovements(Collections.emptyList())
                    .build();
        }
    }

    private User resolveUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private InterviewSession resolveOwnedSession(User user, Long sessionId) {
        return sessionRepository.findByIdAndUser(sessionId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Interview session not found"));
    }
}
