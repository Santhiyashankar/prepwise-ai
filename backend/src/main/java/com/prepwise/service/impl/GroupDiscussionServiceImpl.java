package com.prepwise.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepwise.dto.request.gd.SendGDMessageRequest;
import com.prepwise.dto.request.gd.StartGDRequest;
import com.prepwise.dto.response.gd.GDEvaluationResponse;
import com.prepwise.dto.response.gd.GDHistoryItemResponse;
import com.prepwise.dto.response.gd.GDMessageResponse;
import com.prepwise.dto.response.gd.GDParticipantResponse;
import com.prepwise.dto.response.gd.GDReportResponse;
import com.prepwise.dto.response.gd.GDSessionResponse;
import com.prepwise.dto.response.gd.SendGDMessageResponse;
import com.prepwise.entity.ActivityLog;
import com.prepwise.entity.ActivityType;
import com.prepwise.entity.GDMessageSenderType;
import com.prepwise.entity.GDSessionStatus;
import com.prepwise.entity.GroupDiscussionEvaluation;
import com.prepwise.entity.GroupDiscussionMessage;
import com.prepwise.entity.GroupDiscussionParticipant;
import com.prepwise.entity.GroupDiscussionSession;
import com.prepwise.entity.ParticipantPersonality;
import com.prepwise.entity.User;
import com.prepwise.entity.UserStats;
import com.prepwise.exception.ApiException;
import com.prepwise.exception.ResourceNotFoundException;
import com.prepwise.repository.ActivityLogRepository;
import com.prepwise.repository.GroupDiscussionEvaluationRepository;
import com.prepwise.repository.GroupDiscussionMessageRepository;
import com.prepwise.repository.GroupDiscussionParticipantRepository;
import com.prepwise.repository.GroupDiscussionSessionRepository;
import com.prepwise.repository.UserRepository;
import com.prepwise.repository.UserStatsRepository;
import com.prepwise.service.AIService;
import com.prepwise.service.GDContext;
import com.prepwise.service.GroupDiscussionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class GroupDiscussionServiceImpl implements GroupDiscussionService {

    private static final List<String> NAME_POOL = List.of(
            "Priya", "Arjun", "Sneha", "Rahul", "Kavya", "Vikram",
            "Ananya", "Rohan", "Meera", "Karthik", "Isha", "Aditya"
    );

    private final UserRepository userRepository;
    private final GroupDiscussionSessionRepository sessionRepository;
    private final GroupDiscussionParticipantRepository participantRepository;
    private final GroupDiscussionMessageRepository messageRepository;
    private final GroupDiscussionEvaluationRepository evaluationRepository;
    private final ActivityLogRepository activityLogRepository;
    private final UserStatsRepository userStatsRepository;
    private final AIService aiService;
    private final ObjectMapper objectMapper;

    @Override
    public GDSessionResponse startDiscussion(String email, StartGDRequest request) {
        User user = resolveUser(email);

        String topic = aiService.generateGroupDiscussionTopic(request.getCategory().name(), request.getDifficulty().name());

        GroupDiscussionSession session = GroupDiscussionSession.builder()
                .user(user)
                .category(request.getCategory())
                .difficulty(request.getDifficulty())
                .participantCount(request.getParticipantCount())
                .durationMinutes(request.getDurationMinutes())
                .topic(topic)
                .status(GDSessionStatus.IN_PROGRESS)
                .build();
        GroupDiscussionSession savedSession = sessionRepository.save(session);

        List<GroupDiscussionParticipant> participants = createParticipants(savedSession, request.getParticipantCount());

        List<GroupDiscussionMessage> initialMessages = new ArrayList<>();
        int sequence = 1;

        GroupDiscussionMessage systemMessage = messageRepository.save(GroupDiscussionMessage.builder()
                .session(savedSession)
                .senderType(GDMessageSenderType.SYSTEM)
                .senderName("Moderator")
                .content("Today's group discussion topic is: \"" + topic + "\". You have "
                        + request.getDurationMinutes() + " minutes. Let's begin.")
                .sequenceNumber(sequence++)
                .build());
        initialMessages.add(systemMessage);

        GroupDiscussionParticipant opener = participants.stream()
                .filter(participant -> participant.getPersonality() == ParticipantPersonality.TEAM_LEADER)
                .findFirst()
                .orElse(participants.get(0));

        GDContext context = buildContext(savedSession, participants, List.of());
        String openingLine = aiService.generateParticipantMessage(context, opener.getName(), opener.getPersonality().name());

        GroupDiscussionMessage openingMessage = messageRepository.save(GroupDiscussionMessage.builder()
                .session(savedSession)
                .participant(opener)
                .senderType(GDMessageSenderType.AI_PARTICIPANT)
                .senderName(opener.getName())
                .content(openingLine)
                .sequenceNumber(sequence)
                .build());
        initialMessages.add(openingMessage);

        return buildSessionResponse(savedSession, participants, initialMessages);
    }

    @Override
    public SendGDMessageResponse sendMessage(String email, Long sessionId, SendGDMessageRequest request) {
        User user = resolveUser(email);
        GroupDiscussionSession session = resolveOwnedSession(user, sessionId);

        if (session.getStatus() != GDSessionStatus.IN_PROGRESS) {
            throw new ApiException("This discussion has already ended", HttpStatus.BAD_REQUEST);
        }

        List<GroupDiscussionParticipant> participants = participantRepository.findBySessionOrderByIdAsc(session);
        List<GroupDiscussionMessage> existingMessages = messageRepository.findBySessionOrderBySequenceNumberAsc(session);

        int nextSequence = existingMessages.size() + 1;

        GroupDiscussionMessage userMessage = messageRepository.save(GroupDiscussionMessage.builder()
                .session(session)
                .senderType(GDMessageSenderType.USER)
                .senderName("You")
                .content(request.getContent())
                .sequenceNumber(nextSequence)
                .build());

        long aiMessageCount = existingMessages.stream()
                .filter(message -> message.getSenderType() == GDMessageSenderType.AI_PARTICIPANT)
                .count();
        GroupDiscussionParticipant nextSpeaker = participants.get((int) (aiMessageCount % participants.size()));

        List<GroupDiscussionMessage> transcriptSoFar = new ArrayList<>(existingMessages);
        transcriptSoFar.add(userMessage);

        GDContext context = buildContext(session, participants, transcriptSoFar);
        String aiReply = aiService.generateParticipantMessage(context, nextSpeaker.getName(), nextSpeaker.getPersonality().name());

        GroupDiscussionMessage aiMessage = messageRepository.save(GroupDiscussionMessage.builder()
                .session(session)
                .participant(nextSpeaker)
                .senderType(GDMessageSenderType.AI_PARTICIPANT)
                .senderName(nextSpeaker.getName())
                .content(aiReply)
                .sequenceNumber(nextSequence + 1)
                .build());

        return SendGDMessageResponse.builder()
                .userMessage(mapToMessageResponse(userMessage))
                .aiMessages(List.of(mapToMessageResponse(aiMessage)))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public GDSessionResponse getSession(String email, Long sessionId) {
        User user = resolveUser(email);
        GroupDiscussionSession session = resolveOwnedSession(user, sessionId);
        List<GroupDiscussionParticipant> participants = participantRepository.findBySessionOrderByIdAsc(session);
        List<GroupDiscussionMessage> messages = messageRepository.findBySessionOrderBySequenceNumberAsc(session);
        return buildSessionResponse(session, participants, messages);
    }

    @Override
    public GDReportResponse finishDiscussion(String email, Long sessionId) {
        User user = resolveUser(email);
        GroupDiscussionSession session = resolveOwnedSession(user, sessionId);

        if (session.getStatus() == GDSessionStatus.IN_PROGRESS) {
            List<GroupDiscussionParticipant> participants = participantRepository.findBySessionOrderByIdAsc(session);
            List<GroupDiscussionMessage> messages = messageRepository.findBySessionOrderBySequenceNumberAsc(session);

            boolean userParticipated = messages.stream()
                    .anyMatch(message -> message.getSenderType() == GDMessageSenderType.USER);

            GDContext context = buildContext(session, participants, messages);
            GDEvaluationResponse evaluationResult = userParticipated
                    ? aiService.evaluateGroupDiscussion(context)
                    : emptyEvaluation();

            GroupDiscussionEvaluation evaluation = GroupDiscussionEvaluation.builder()
                    .session(session)
                    .overallScore(evaluationResult.getOverallScore())
                    .communicationScore(evaluationResult.getCommunicationScore())
                    .confidenceScore(evaluationResult.getConfidenceScore())
                    .fluencyScore(evaluationResult.getFluencyScore())
                    .grammarScore(evaluationResult.getGrammarScore())
                    .leadershipScore(evaluationResult.getLeadershipScore())
                    .teamworkScore(evaluationResult.getTeamworkScore())
                    .criticalThinkingScore(evaluationResult.getCriticalThinkingScore())
                    .relevanceScore(evaluationResult.getRelevanceScore())
                    .vocabularyScore(evaluationResult.getVocabularyScore())
                    .summary(evaluationResult.getSummary())
                    .strengthsJson(toJson(evaluationResult.getStrengths()))
                    .weaknessesJson(toJson(evaluationResult.getWeaknesses()))
                    .improvementTipsJson(toJson(evaluationResult.getImprovementTips()))
                    .suggestedResponsesJson(toJson(evaluationResult.getSuggestedResponses()))
                    .build();
            evaluationRepository.save(evaluation);

            session.setStatus(GDSessionStatus.COMPLETED);
            session.setCompletedAt(LocalDateTime.now());
            sessionRepository.save(session);

            updateUserStatsAndActivity(user, session, evaluationResult.getOverallScore());
        }

        return getReport(email, sessionId);
    }

    @Override
    @Transactional(readOnly = true)
    public GDReportResponse getReport(String email, Long sessionId) {
        User user = resolveUser(email);
        GroupDiscussionSession session = resolveOwnedSession(user, sessionId);

        GroupDiscussionEvaluation evaluation = evaluationRepository.findBySession(session)
                .orElseThrow(() -> new ResourceNotFoundException("This discussion has not been evaluated yet"));

        long durationSeconds = session.getCompletedAt() != null
                ? Duration.between(session.getStartedAt(), session.getCompletedAt()).getSeconds()
                : 0;

        return GDReportResponse.builder()
                .sessionId(session.getId())
                .topic(session.getTopic())
                .category(session.getCategory())
                .difficulty(session.getDifficulty())
                .durationSeconds(durationSeconds)
                .overallScore(evaluation.getOverallScore())
                .communicationScore(evaluation.getCommunicationScore())
                .confidenceScore(evaluation.getConfidenceScore())
                .fluencyScore(evaluation.getFluencyScore())
                .grammarScore(evaluation.getGrammarScore())
                .leadershipScore(evaluation.getLeadershipScore())
                .teamworkScore(evaluation.getTeamworkScore())
                .criticalThinkingScore(evaluation.getCriticalThinkingScore())
                .relevanceScore(evaluation.getRelevanceScore())
                .vocabularyScore(evaluation.getVocabularyScore())
                .summary(evaluation.getSummary())
                .strengths(fromJson(evaluation.getStrengthsJson()))
                .weaknesses(fromJson(evaluation.getWeaknessesJson()))
                .improvementTips(fromJson(evaluation.getImprovementTipsJson()))
                .suggestedResponses(fromJson(evaluation.getSuggestedResponsesJson()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GDHistoryItemResponse> getHistory(String email) {
        User user = resolveUser(email);
        return sessionRepository.findByUserOrderByStartedAtDesc(user).stream()
                .map(this::mapToHistoryItem)
                .collect(Collectors.toList());
    }

    @Override
    public GDSessionResponse retryDiscussion(String email, Long sessionId) {
        User user = resolveUser(email);
        GroupDiscussionSession original = resolveOwnedSession(user, sessionId);

        StartGDRequest request = new StartGDRequest(
                original.getCategory(), original.getDifficulty(),
                original.getParticipantCount(), original.getDurationMinutes()
        );
        return startDiscussion(email, request);
    }

    @Override
    public void deleteSession(String email, Long sessionId) {
        User user = resolveUser(email);
        GroupDiscussionSession session = resolveOwnedSession(user, sessionId);

        evaluationRepository.findBySession(session).ifPresent(evaluationRepository::delete);
        messageRepository.deleteBySession(session);
        participantRepository.deleteBySession(session);
        sessionRepository.delete(session);
    }

    private List<GroupDiscussionParticipant> createParticipants(GroupDiscussionSession session, int count) {
        List<ParticipantPersonality> personalities = new ArrayList<>(List.of(ParticipantPersonality.values()));
        Collections.shuffle(personalities);
        if (!personalities.contains(ParticipantPersonality.TEAM_LEADER)) {
            personalities.set(0, ParticipantPersonality.TEAM_LEADER);
        } else {
            personalities.remove(ParticipantPersonality.TEAM_LEADER);
            personalities.add(0, ParticipantPersonality.TEAM_LEADER);
        }

        List<String> names = new ArrayList<>(NAME_POOL);
        Collections.shuffle(names);

        List<GroupDiscussionParticipant> participants = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            participants.add(participantRepository.save(GroupDiscussionParticipant.builder()
                    .session(session)
                    .name(names.get(i % names.size()))
                    .personality(personalities.get(i % personalities.size()))
                    .colorIndex(i)
                    .build()));
        }
        return participants;
    }

    private void updateUserStatsAndActivity(User user, GroupDiscussionSession session, double overallScore) {
        activityLogRepository.save(ActivityLog.builder()
                .user(user)
                .activityType(ActivityType.GROUP_DISCUSSION)
                .title(session.getCategory() + " GD completed")
                .description(session.getParticipantCount() + " participants \u00b7 " + session.getDifficulty())
                .score(overallScore)
                .build());

        UserStats stats = userStatsRepository.findByUser(user)
                .orElseGet(() -> userStatsRepository.save(UserStats.builder().user(user).updatedAt(LocalDateTime.now()).build()));

        stats.setCurrentXp(stats.getCurrentXp() + (int) Math.round(overallScore * 0.5));
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

    private GDEvaluationResponse emptyEvaluation() {
        return GDEvaluationResponse.builder()
                .overallScore(0)
                .communicationScore(0)
                .confidenceScore(0)
                .fluencyScore(0)
                .grammarScore(0)
                .leadershipScore(0)
                .teamworkScore(0)
                .criticalThinkingScore(0)
                .relevanceScore(0)
                .vocabularyScore(0)
                .summary("You did not contribute any messages to this discussion.")
                .strengths(Collections.emptyList())
                .weaknesses(List.of("No participation recorded"))
                .improvementTips(List.of("Try to contribute at least a few points next time"))
                .suggestedResponses(Collections.emptyList())
                .build();
    }

    private GDContext buildContext(
            GroupDiscussionSession session, List<GroupDiscussionParticipant> participants, List<GroupDiscussionMessage> messages) {
        GDContext context = new GDContext();
        context.topic = session.getTopic();
        context.category = session.getCategory().name();
        context.difficulty = session.getDifficulty().name();
        context.participants = participants.stream()
                .map(participant -> new GDContext.ParticipantInfo(participant.getName(), participant.getPersonality().name()))
                .collect(Collectors.toList());
        context.transcript = messages.stream()
                .map(message -> new GDContext.TranscriptEntry(message.getSenderName(), message.getContent()))
                .collect(Collectors.toList());
        return context;
    }

    private GDSessionResponse buildSessionResponse(
            GroupDiscussionSession session, List<GroupDiscussionParticipant> participants, List<GroupDiscussionMessage> messages) {
        return GDSessionResponse.builder()
                .id(session.getId())
                .category(session.getCategory())
                .difficulty(session.getDifficulty())
                .participantCount(session.getParticipantCount())
                .durationMinutes(session.getDurationMinutes())
                .topic(session.getTopic())
                .status(session.getStatus())
                .startedAt(session.getStartedAt())
                .completedAt(session.getCompletedAt())
                .participants(participants.stream().map(this::mapToParticipantResponse).collect(Collectors.toList()))
                .messages(messages.stream().map(this::mapToMessageResponse).collect(Collectors.toList()))
                .build();
    }

    private GDParticipantResponse mapToParticipantResponse(GroupDiscussionParticipant participant) {
        return GDParticipantResponse.builder()
                .id(participant.getId())
                .name(participant.getName())
                .personality(participant.getPersonality())
                .colorIndex(participant.getColorIndex())
                .build();
    }

    private GDMessageResponse mapToMessageResponse(GroupDiscussionMessage message) {
        return GDMessageResponse.builder()
                .id(message.getId())
                .senderType(message.getSenderType())
                .senderName(message.getSenderName())
                .participantId(message.getParticipant() != null ? message.getParticipant().getId() : null)
                .colorIndex(message.getParticipant() != null ? message.getParticipant().getColorIndex() : -1)
                .content(message.getContent())
                .sequenceNumber(message.getSequenceNumber())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private GDHistoryItemResponse mapToHistoryItem(GroupDiscussionSession session) {
        long durationSeconds = session.getCompletedAt() != null
                ? Duration.between(session.getStartedAt(), session.getCompletedAt()).getSeconds()
                : 0;

        Double overallScore = evaluationRepository.findBySession(session)
                .map(GroupDiscussionEvaluation::getOverallScore)
                .orElse(null);

        return GDHistoryItemResponse.builder()
                .sessionId(session.getId())
                .topic(session.getTopic())
                .category(session.getCategory())
                .difficulty(session.getDifficulty())
                .status(session.getStatus())
                .overallScore(overallScore)
                .durationSeconds(durationSeconds)
                .startedAt(session.getStartedAt())
                .completedAt(session.getCompletedAt())
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

    private GroupDiscussionSession resolveOwnedSession(User user, Long sessionId) {
        return sessionRepository.findByIdAndUser(sessionId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Group discussion session not found"));
    }
}
