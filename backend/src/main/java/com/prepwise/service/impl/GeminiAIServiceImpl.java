package com.prepwise.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepwise.dto.response.coding.CodingReportResponse;
import com.prepwise.dto.response.gd.GDEvaluationResponse;
import com.prepwise.dto.response.interview.DailyPlanItemResponse;
import com.prepwise.dto.response.interview.ImprovementPlanResponse;
import com.prepwise.dto.response.interview.InterviewEvaluationResponse;
import com.prepwise.dto.response.resume.ResumeAnalysisResponse;
import com.prepwise.exception.ApiException;
import com.prepwise.service.AIService;
import com.prepwise.service.CodingReviewContext;
import com.prepwise.service.GDContext;
import com.prepwise.service.InterviewContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GeminiAIServiceImpl implements AIService {

    private static final int MAX_RESUME_CHARS = 12000;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.model}")
    private String model;

    public GeminiAIServiceImpl(RestTemplate geminiRestTemplate, ObjectMapper objectMapper) {
        this.restTemplate = geminiRestTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResumeAnalysisResponse analyzeResume(String resumeText) {
        String prompt = buildAnalysisPrompt(truncate(resumeText));
        String rawJson = callGemini(prompt);
        return parseAnalysisResponse(rawJson);
    }

    @Override
    public String generateResumeSummary(String resumeText) {
        String prompt = "Write a concise, professional 3-sentence summary of the following resume. "
                + "Return plain text only, no markdown, no headings.\n\nResume:\n" + truncate(resumeText);
        return callGeminiPlainText(prompt);
    }

    @Override
    public List<String> suggestResumeImprovements(String resumeText) {
        String prompt = "Analyze the following resume and return a JSON array of concise, actionable "
                + "improvement suggestions (5 to 8 items, each under 20 words). "
                + "Return ONLY a JSON array of strings, no markdown, no extra text.\n\nResume:\n"
                + truncate(resumeText);
        String rawJson = callGemini(prompt);
        return parseStringArray(rawJson);
    }

    @Override
    public String generateGroupDiscussionTopic(String category, String difficulty) {
        String prompt = """
                You are moderating a placement-style group discussion. Generate ONE realistic group \
                discussion topic for the category "%s" at "%s" difficulty. The topic should be a single \
                clear statement or question suitable for a 5-10 person discussion.

                Respond with ONLY a single valid JSON object (no markdown fences, no commentary) in this \
                exact shape:
                { "topic": string }
                """.formatted(category, difficulty);

        String rawJson = callGemini(prompt);
        try {
            JsonNode node = objectMapper.readTree(sanitizeJson(rawJson));
            return textOrEmpty(node, "topic");
        } catch (Exception ex) {
            log.error("Failed to parse Gemini GD topic response: {}", ex.getMessage());
            throw new ApiException("Failed to generate a discussion topic", HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public String generateParticipantMessage(GDContext context, String participantName, String personality) {
        String prompt = buildParticipantPrompt(context, participantName, personality);
        return callGeminiPlainText(prompt).trim();
    }

    @Override
    public GDEvaluationResponse evaluateGroupDiscussion(GDContext context) {
        String prompt = buildGDEvaluationPrompt(context);
        String rawJson = callGemini(prompt);
        try {
            JsonNode node = objectMapper.readTree(sanitizeJson(rawJson));

            return GDEvaluationResponse.builder()
                    .overallScore(node.has("overallScore") ? node.get("overallScore").asDouble() : 0.0)
                    .communicationScore(node.has("communicationScore") ? node.get("communicationScore").asDouble() : 0.0)
                    .confidenceScore(node.has("confidenceScore") ? node.get("confidenceScore").asDouble() : 0.0)
                    .fluencyScore(node.has("fluencyScore") ? node.get("fluencyScore").asDouble() : 0.0)
                    .grammarScore(node.has("grammarScore") ? node.get("grammarScore").asDouble() : 0.0)
                    .leadershipScore(node.has("leadershipScore") ? node.get("leadershipScore").asDouble() : 0.0)
                    .teamworkScore(node.has("teamworkScore") ? node.get("teamworkScore").asDouble() : 0.0)
                    .criticalThinkingScore(node.has("criticalThinkingScore") ? node.get("criticalThinkingScore").asDouble() : 0.0)
                    .relevanceScore(node.has("relevanceScore") ? node.get("relevanceScore").asDouble() : 0.0)
                    .vocabularyScore(node.has("vocabularyScore") ? node.get("vocabularyScore").asDouble() : 0.0)
                    .summary(textOrEmpty(node, "summary"))
                    .strengths(arrayOrEmpty(node, "strengths"))
                    .weaknesses(arrayOrEmpty(node, "weaknesses"))
                    .improvementTips(arrayOrEmpty(node, "improvementTips"))
                    .suggestedResponses(arrayOrEmpty(node, "suggestedResponses"))
                    .build();
        } catch (Exception ex) {
            log.error("Failed to parse Gemini GD evaluation response: {}", ex.getMessage());
            throw new ApiException("Failed to generate the discussion evaluation", HttpStatus.BAD_GATEWAY);
        }
    }

    private String buildParticipantPrompt(GDContext context, String participantName, String personality) {
        StringBuilder rosterBuilder = new StringBuilder();
        for (GDContext.ParticipantInfo participant : context.participants) {
            rosterBuilder.append("- ").append(participant.name).append(" (").append(participant.personality).append(")\n");
        }

        StringBuilder transcriptBuilder = new StringBuilder();
        for (GDContext.TranscriptEntry entry : context.transcript) {
            transcriptBuilder.append(entry.senderName).append(": ").append(entry.content).append("\n");
        }

        String personalityGuidance = switch (personality) {
            case "TEAM_LEADER" -> "You steer the discussion, summarize points, and invite others to speak.";
            case "LOGICAL_THINKER" -> "You back every point with structured reasoning and data.";
            case "AGGRESSIVE_SPEAKER" -> "You are assertive, interrupt-prone, and push your viewpoint strongly.";
            case "CALM_SPEAKER" -> "You are measured, diplomatic, and focus on balancing viewpoints.";
            case "CREATIVE_THINKER" -> "You bring unconventional angles and analogies to the topic.";
            case "QUESTIONER" -> "You probe others with clarifying and challenging questions.";
            default -> "You participate naturally and constructively.";
        };

        return """
                You are role-playing as "%s" in a live group discussion, with the personality: %s
                %s

                Discussion topic: %s
                Category: %s, Difficulty: %s

                Other participants in this discussion:
                %s

                Conversation so far:
                %s

                Speak as %s would, in 1-3 sentences, reacting naturally to what was just said and staying \
                in character. Do not repeat points already made. Do not prefix your response with your name. \
                Return plain text only, no markdown, no quotation marks.
                """.formatted(
                participantName, personality, personalityGuidance, context.topic, context.category,
                context.difficulty, rosterBuilder, transcriptBuilder.isEmpty() ? "(discussion is just starting)"
                        : transcriptBuilder.toString(), participantName
        );
    }

    private String buildGDEvaluationPrompt(GDContext context) {
        StringBuilder transcriptBuilder = new StringBuilder();
        for (GDContext.TranscriptEntry entry : context.transcript) {
            transcriptBuilder.append(entry.senderName).append(": ").append(entry.content).append("\n");
        }

        return """
                You are an expert placement group-discussion evaluator. Evaluate ONLY the messages from \
                "You" (the human candidate) in the transcript below, in the context of the full discussion.

                Topic: %s
                Category: %s, Difficulty: %s

                Full transcript:
                %s

                Evaluate the candidate's contributions and respond with ONLY a single valid JSON object \
                (no markdown fences, no commentary) in this exact shape:
                {
                  "overallScore": number (0 to 100),
                  "communicationScore": number (0 to 100),
                  "confidenceScore": number (0 to 100),
                  "fluencyScore": number (0 to 100),
                  "grammarScore": number (0 to 100),
                  "leadershipScore": number (0 to 100),
                  "teamworkScore": number (0 to 100),
                  "criticalThinkingScore": number (0 to 100),
                  "relevanceScore": number (0 to 100),
                  "vocabularyScore": number (0 to 100),
                  "summary": string,
                  "strengths": string[],
                  "weaknesses": string[],
                  "improvementTips": string[],
                  "suggestedResponses": string[]
                }
                """.formatted(context.topic, context.category, context.difficulty, transcriptBuilder);
    }

    @Override
    public String generateNextInterviewQuestion(InterviewContext context) {
        String prompt = buildQuestionPrompt(context);
        String rawJson = callGemini(prompt);
        try {
            JsonNode node = objectMapper.readTree(sanitizeJson(rawJson));
            return textOrEmpty(node, "question");
        } catch (Exception ex) {
            log.error("Failed to parse Gemini question response: {}", ex.getMessage());
            throw new ApiException("Failed to generate the next interview question", HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public InterviewEvaluationResponse evaluateInterviewAnswer(InterviewContext context, String question, String answer) {
        String prompt = buildEvaluationPrompt(context, question, answer);
        String rawJson = callGemini(prompt);
        try {
            JsonNode node = objectMapper.readTree(sanitizeJson(rawJson));
            return InterviewEvaluationResponse.builder()
                    .score(node.has("score") ? node.get("score").asDouble() : 0.0)
                    .technicalKnowledge(node.has("technicalKnowledge") ? node.get("technicalKnowledge").asDouble() : 0.0)
                    .communication(node.has("communication") ? node.get("communication").asDouble() : 0.0)
                    .confidence(node.has("confidence") ? node.get("confidence").asDouble() : 0.0)
                    .problemSolving(node.has("problemSolving") ? node.get("problemSolving").asDouble() : 0.0)
                    .timeManagement(node.has("timeManagement") ? node.get("timeManagement").asDouble() : 0.0)
                    .strengths(arrayOrEmpty(node, "strengths"))
                    .mistakes(arrayOrEmpty(node, "mistakes"))
                    .idealAnswer(textOrEmpty(node, "idealAnswer"))
                    .followUpQuestion(textOrEmpty(node, "followUpQuestion"))
                    .build();
        } catch (Exception ex) {
            log.error("Failed to parse Gemini evaluation response: {}", ex.getMessage());
            throw new ApiException("Failed to evaluate the interview answer", HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public ImprovementPlanResponse generateInterviewImprovementPlan(InterviewContext context, double overallScore) {
        String prompt = buildImprovementPlanPrompt(context, overallScore);
        String rawJson = callGemini(prompt);
        try {
            JsonNode node = objectMapper.readTree(sanitizeJson(rawJson));
            List<DailyPlanItemResponse> dailyPlan = new ArrayList<>();
            JsonNode planNode = node.get("dailyPlan");
            if (planNode != null && planNode.isArray()) {
                for (JsonNode dayNode : planNode) {
                    dailyPlan.add(DailyPlanItemResponse.builder()
                            .day(dayNode.has("day") ? dayNode.get("day").asInt() : dailyPlan.size() + 1)
                            .focusArea(textOrEmpty(dayNode, "focusArea"))
                            .tasks(arrayOrEmpty(dayNode, "tasks"))
                            .build());
                }
            }

            return ImprovementPlanResponse.builder()
                    .dailyPlan(dailyPlan)
                    .recommendedTopics(arrayOrEmpty(node, "recommendedTopics"))
                    .projectsToRevise(arrayOrEmpty(node, "projectsToRevise"))
                    .resumeImprovements(arrayOrEmpty(node, "resumeImprovements"))
                    .build();
        } catch (Exception ex) {
            log.error("Failed to parse Gemini improvement plan response: {}", ex.getMessage());
            throw new ApiException("Failed to generate the improvement plan", HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public CodingReportResponse reviewCode(CodingReviewContext context) {
        String prompt = buildCodeReviewPrompt(context);
        String rawJson = callGemini(prompt);
        try {
            JsonNode node = objectMapper.readTree(sanitizeJson(rawJson));

            return CodingReportResponse.builder()
                    .overallScore(node.has("overallScore") ? node.get("overallScore").asDouble() : 0.0)
                    .correctnessScore(node.has("correctnessScore") ? node.get("correctnessScore").asDouble() : 0.0)
                    .readabilityScore(node.has("readabilityScore") ? node.get("readabilityScore").asDouble() : 0.0)
                    .bestPracticesScore(node.has("bestPracticesScore") ? node.get("bestPracticesScore").asDouble() : 0.0)
                    .edgeCaseScore(node.has("edgeCaseScore") ? node.get("edgeCaseScore").asDouble() : 0.0)
                    .timeComplexity(textOrEmpty(node, "timeComplexity"))
                    .spaceComplexity(textOrEmpty(node, "spaceComplexity"))
                    .strengths(arrayOrEmpty(node, "strengths"))
                    .weaknesses(arrayOrEmpty(node, "weaknesses"))
                    .optimizationSuggestions(arrayOrEmpty(node, "optimizationSuggestions"))
                    .optimizedSolution(textOrEmpty(node, "optimizedSolution"))
                    .build();
        } catch (Exception ex) {
            log.error("Failed to parse Gemini code review response: {}", ex.getMessage());
            throw new ApiException("Failed to generate the AI code review", HttpStatus.BAD_GATEWAY);
        }
    }
    @Override
    public String chatWithMentor(String message) {
        String prompt = """
            You are PrepWise AI's personal career and interview mentor.

            The user is a final-year engineering student preparing for software
            engineering placements.

            Help the user with:
            - Java and Spring Boot
            - JavaScript and React
            - DSA and coding interviews
            - Technical interview preparation
            - HR interview preparation
            - Resume improvement
            - Group discussion and communication practice
            - Career and placement guidance

            Give practical, beginner-friendly answers.
            If the user asks a technical question, explain it simply and include
            a small example when useful.
            If the user asks for interview practice, act like an interviewer.
            Do not make up information about the user's skills or experience.

            User message:
            %s

            Respond naturally and directly. Do not use JSON.
            """.formatted(message == null ? "" : message.trim());

        return callGeminiPlainText(prompt).trim();
    }

    private String buildCodeReviewPrompt(CodingReviewContext context) {
        return """
                You are a senior software engineer performing a code review, similar to a HackerRank or \
                LeetCode AI reviewer.

                Problem: %s
                Description: %s
                Difficulty: %s, Topic: %s, Company Context: %s
                Language: %s
                Test results: %d of %d test cases passed.

                Candidate's code:
                %s

                Evaluate the code across correctness, time complexity, space complexity, readability, \
                best practices, and edge case handling, taking the stated difficulty and topic into \
                account (e.g. hold a Hard/DP submission to a higher bar than an Easy/Arrays one). \
                Provide an optimized version of the solution in the same language. Respond with ONLY a \
                single valid JSON object (no markdown fences, no commentary) in this exact shape:
                {
                  "overallScore": number (0 to 100),
                  "correctnessScore": number (0 to 100),
                  "readabilityScore": number (0 to 100),
                  "bestPracticesScore": number (0 to 100),
                  "edgeCaseScore": number (0 to 100),
                  "timeComplexity": string (e.g. "O(n log n)"),
                  "spaceComplexity": string (e.g. "O(n)"),
                  "strengths": string[],
                  "weaknesses": string[],
                  "optimizationSuggestions": string[],
                  "optimizedSolution": string
                }
                """.formatted(
                context.problemTitle, context.problemDescription,
                context.difficulty, context.topic, context.company != null ? context.company : "General",
                context.language, context.passedTestCases, context.totalTestCases, context.sourceCode
        );
    }

    private String buildQuestionPrompt(InterviewContext context) {
        StringBuilder historyBuilder = new StringBuilder();
        for (InterviewContext.QAPair pair : context.history) {
            historyBuilder.append("Q: ").append(pair.question).append("\nA: ").append(pair.answer)
                    .append(" (scored ").append(pair.score).append("/100)\n\n");
        }

        return """
                You are a senior technical interviewer conducting a live mock interview.

                Interview Role: %s
                Experience Level: %s
                Difficulty: %s
                Interview Style: %s
                Question %d of %d

                Previous questions and answers in this interview:
                %s

                Generate ONE realistic interview question appropriate for this role, experience level, \
                difficulty, and style. If there is interview history, the question should build naturally \
                on the candidate's previous answer rather than repeating topics already covered.

                Respond with ONLY a single valid JSON object (no markdown fences, no commentary) in this \
                exact shape:
                { "question": string }
                """.formatted(
                context.role, context.experience, context.difficulty, context.style,
                context.questionNumber, context.totalQuestions,
                historyBuilder.isEmpty() ? "(none yet — this is the first question)" : historyBuilder.toString()
        );
    }

    private String buildEvaluationPrompt(InterviewContext context, String question, String answer) {
        return """
                You are a senior technical interviewer evaluating a candidate's answer in a live mock interview.

                Interview Role: %s
                Experience Level: %s
                Difficulty: %s

                Question asked: %s

                Candidate's answer: %s

                Evaluate the answer honestly and constructively across five dimensions. Respond with ONLY \
                a single valid JSON object (no markdown fences, no commentary) in this exact shape:
                {
                  "score": number (0 to 100, overall quality of this answer),
                  "technicalKnowledge": number (0 to 100),
                  "communication": number (0 to 100, clarity and structure of the answer),
                  "confidence": number (0 to 100, decisiveness and assurance conveyed),
                  "problemSolving": number (0 to 100, reasoning and approach quality),
                  "timeManagement": number (0 to 100, conciseness relative to depth required),
                  "strengths": string[],
                  "mistakes": string[],
                  "idealAnswer": string,
                  "followUpQuestion": string
                }
                """.formatted(context.role, context.experience, context.difficulty, question,
                answer == null || answer.isBlank() ? "(no answer provided — candidate skipped this question)" : answer);
    }

    private String buildImprovementPlanPrompt(InterviewContext context, double overallScore) {
        StringBuilder historyBuilder = new StringBuilder();
        for (InterviewContext.QAPair pair : context.history) {
            historyBuilder.append("- ").append(pair.question).append(" (scored ").append(pair.score).append("/100)\n");
        }

        return """
                You are an AI career coach. A candidate just completed a mock interview for the role of %s \
                at %s difficulty with an overall score of %.0f/100.

                Questions asked and their scores:
                %s

                Generate a personalized 7-day improvement plan. Respond with ONLY a single valid JSON \
                object (no markdown fences, no commentary) in this exact shape:
                {
                  "dailyPlan": [
                    { "day": number, "focusArea": string, "tasks": string[] }
                  ],
                  "recommendedTopics": string[],
                  "projectsToRevise": string[],
                  "resumeImprovements": string[]
                }
                The dailyPlan array must contain exactly 7 entries, one per day.
                """.formatted(context.role, context.difficulty, overallScore,
                historyBuilder.isEmpty() ? "(no questions recorded)" : historyBuilder.toString());
    }

    private String buildAnalysisPrompt(String resumeText) {
        return """
                You are an expert resume reviewer and ATS specialist. Analyze the resume text below and \
                respond with ONLY a single valid JSON object (no markdown fences, no commentary) with \
                exactly these keys:

                {
                  "professionalSummary": string,
                  "careerObjective": string,
                  "professionalHeadline": string,
                  "strengths": string[],
                  "weaknesses": string[],
                  "missingSections": string[],
                  "grammarSuggestions": string[],
                  "formattingSuggestions": string[],
                  "projectImprovements": string[],
                  "experienceImprovements": string[],
                  "skillSuggestions": string[],
                  "overallRating": number (0 to 100)
                }

                Resume text:
                """ + resumeText;
    }

    private ResumeAnalysisResponse parseAnalysisResponse(String rawJson) {
        try {
            JsonNode node = objectMapper.readTree(sanitizeJson(rawJson));

            return ResumeAnalysisResponse.builder()
                    .professionalSummary(textOrEmpty(node, "professionalSummary"))
                    .careerObjective(textOrEmpty(node, "careerObjective"))
                    .professionalHeadline(textOrEmpty(node, "professionalHeadline"))
                    .strengths(arrayOrEmpty(node, "strengths"))
                    .weaknesses(arrayOrEmpty(node, "weaknesses"))
                    .missingSections(arrayOrEmpty(node, "missingSections"))
                    .grammarSuggestions(arrayOrEmpty(node, "grammarSuggestions"))
                    .formattingSuggestions(arrayOrEmpty(node, "formattingSuggestions"))
                    .projectImprovements(arrayOrEmpty(node, "projectImprovements"))
                    .experienceImprovements(arrayOrEmpty(node, "experienceImprovements"))
                    .skillSuggestions(arrayOrEmpty(node, "skillSuggestions"))
                    .overallRating(node.has("overallRating") ? node.get("overallRating").asDouble() : 0.0)
                    .build();
        } catch (Exception ex) {
            log.error("Failed to parse Gemini analysis response: {}", ex.getMessage());
            throw new ApiException("Failed to parse AI analysis response", HttpStatus.BAD_GATEWAY);
        }
    }

    private List<String> parseStringArray(String rawJson) {
        try {
            JsonNode node = objectMapper.readTree(sanitizeJson(rawJson));
            List<String> results = new ArrayList<>();
            if (node.isArray()) {
                node.forEach(item -> results.add(item.asText()));
            }
            return results;
        } catch (Exception ex) {
            log.error("Failed to parse Gemini suggestions response: {}", ex.getMessage());
            throw new ApiException("Failed to parse AI suggestions response", HttpStatus.BAD_GATEWAY);
        }
    }

    private String callGemini(String prompt) {
        return callGeminiInternal(prompt, true);
    }

    private String callGeminiPlainText(String prompt) {
        return callGeminiInternal(prompt, false);
    }

    private String callGeminiInternal(String prompt, boolean expectJson) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new ApiException("Gemini API key is not configured", HttpStatus.SERVICE_UNAVAILABLE);
        }

        String endpoint = apiUrl + "/v1beta/models/" + model + ":generateContent?key=" + apiKey;

        var textPart = new java.util.HashMap<String, Object>();
        textPart.put("text", prompt);

        var content = new java.util.HashMap<String, Object>();
        content.put("parts", List.of(textPart));

        var requestBody = new java.util.HashMap<String, Object>();
        requestBody.put("contents", List.of(content));

        if (expectJson) {
            var generationConfig = new java.util.HashMap<String, Object>();
            generationConfig.put("responseMimeType", "application/json");
            generationConfig.put("temperature", 0.4);
            requestBody.put("generationConfig", generationConfig);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            JsonNode response = restTemplate.postForObject(endpoint, requestEntity, JsonNode.class);
            return extractTextFromResponse(response);
        } catch (ResourceAccessException ex) {
            log.error("Gemini API timeout: {}", ex.getMessage());
            throw new ApiException("The AI service timed out. Please try again.", HttpStatus.GATEWAY_TIMEOUT);
        } catch (RestClientException ex) {
            log.error("Gemini API call failed: {}", ex.getMessage());
            throw new ApiException("The AI service is currently unavailable. Please try again later.",
                    HttpStatus.BAD_GATEWAY);
        }
    }

    private String extractTextFromResponse(JsonNode response) {
        if (response == null) {
            throw new ApiException("Empty response from AI service", HttpStatus.BAD_GATEWAY);
        }
        JsonNode candidates = response.path("candidates");
        if (!candidates.isArray() || candidates.isEmpty()) {
            throw new ApiException("AI service returned no candidates", HttpStatus.BAD_GATEWAY);
        }
        JsonNode parts = candidates.get(0).path("content").path("parts");
        if (!parts.isArray() || parts.isEmpty()) {
            throw new ApiException("AI service returned an empty response", HttpStatus.BAD_GATEWAY);
        }
        return parts.get(0).path("text").asText();
    }

    private String sanitizeJson(String rawJson) {
        if (rawJson == null) return "{}";
        String trimmed = rawJson.trim();
        if (trimmed.startsWith("```")) {
            trimmed = trimmed.replaceAll("^```json", "").replaceAll("^```", "")
                    .replaceAll("```$", "").trim();
        }
        return trimmed;
    }

    private String textOrEmpty(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asText() : "";
    }

    private List<String> arrayOrEmpty(JsonNode node, String field) {
        List<String> results = new ArrayList<>();
        JsonNode arrayNode = node.get(field);
        if (arrayNode != null && arrayNode.isArray()) {
            arrayNode.forEach(item -> results.add(item.asText()));
        }
        return results;
    }

    private String truncate(String text) {
        if (text == null) return "";
        return text.length() > MAX_RESUME_CHARS ? text.substring(0, MAX_RESUME_CHARS) : text;
    }
}
