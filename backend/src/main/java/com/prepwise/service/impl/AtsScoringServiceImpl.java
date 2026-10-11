package com.prepwise.service.impl;

import com.prepwise.service.AtsScoreBreakdown;
import com.prepwise.service.AtsScoringService;
import com.prepwise.service.ResumeParserService.ParsedResume;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AtsScoringServiceImpl implements AtsScoringService {

    private static final Set<String> ACTION_VERBS = new HashSet<>(Arrays.asList(
            "achieved", "built", "created", "designed", "developed", "engineered", "improved",
            "increased", "reduced", "launched", "led", "managed", "optimized", "implemented",
            "delivered", "automated", "architected", "collaborated", "spearheaded", "streamlined",
            "resolved", "mentored", "analyzed", "deployed", "migrated", "refactored"
    ));

    private static final Pattern LONG_LINE_PATTERN = Pattern.compile(".{200,}");

    @Override
    public AtsScoreBreakdown computeScore(ParsedResume parsed, double keywordMatchPercentage) {
        AtsScoreBreakdown breakdown = new AtsScoreBreakdown();

        double contactScore = scoreContactDetails(parsed);
        double lengthScore = scoreResumeLength(parsed.rawText);
        double sectionScore = scoreSectionCompleteness(parsed);
        double skillsScore = scoreSkills(parsed.skills);
        double keywordScore = (keywordMatchPercentage / 100.0) * 15.0;
        double educationScore = parsed.education != null && !parsed.education.isEmpty() ? 5.0 : 0.0;
        double experienceScore = scoreExperience(parsed.experience);
        double projectsScore = parsed.projects != null && !parsed.projects.isEmpty() ? 5.0 : 0.0;
        double certificationsScore = parsed.certifications != null && !parsed.certifications.isEmpty() ? 5.0 : 0.0;
        double formattingScore = scoreFormatting(parsed.rawText);
        double actionVerbScore = scoreActionVerbs(parsed.experience, parsed.projects);
        double grammarScore = scoreGrammar(parsed.rawText);
        double duplicateWordScore = scoreDuplicateWords(parsed.rawText);

        breakdown.criteriaScores.put("Contact Details", round(contactScore));
        breakdown.criteriaScores.put("Resume Length", round(lengthScore));
        breakdown.criteriaScores.put("Section Completeness", round(sectionScore));
        breakdown.criteriaScores.put("Skills", round(skillsScore));
        breakdown.criteriaScores.put("Keywords", round(keywordScore));
        breakdown.criteriaScores.put("Education", round(educationScore));
        breakdown.criteriaScores.put("Experience", round(experienceScore));
        breakdown.criteriaScores.put("Projects", round(projectsScore));
        breakdown.criteriaScores.put("Certifications", round(certificationsScore));
        breakdown.criteriaScores.put("Formatting", round(formattingScore));
        breakdown.criteriaScores.put("Action Verbs", round(actionVerbScore));
        breakdown.criteriaScores.put("Grammar", round(grammarScore));
        breakdown.criteriaScores.put("Duplicate Words", round(duplicateWordScore));

        double total = breakdown.criteriaScores.values().stream().mapToDouble(Double::doubleValue).sum();
        breakdown.overallScore = round(Math.min(100.0, Math.max(0.0, total)));

        return breakdown;
    }

    private double scoreContactDetails(ParsedResume parsed) {
        double score = 0;
        if (parsed.email != null && !parsed.email.isBlank()) score += 4;
        if (parsed.phone != null && !parsed.phone.isBlank()) score += 3;
        if ((parsed.linkedinUrl != null && !parsed.linkedinUrl.isBlank())
                || (parsed.githubUrl != null && !parsed.githubUrl.isBlank())
                || (parsed.portfolioUrl != null && !parsed.portfolioUrl.isBlank())) {
            score += 3;
        }
        return score;
    }

    private double scoreResumeLength(String rawText) {
        int wordCount = rawText.trim().isEmpty() ? 0 : rawText.trim().split("\\s+").length;
        if (wordCount >= 300 && wordCount <= 800) return 10.0;
        if (wordCount >= 200 && wordCount < 300) return 7.0;
        if (wordCount > 800 && wordCount <= 1000) return 7.0;
        if (wordCount >= 100 && wordCount < 200) return 4.0;
        if (wordCount > 1000) return 4.0;
        return 1.0;
    }

    private double scoreSectionCompleteness(ParsedResume parsed) {
        int present = 0;
        int total = 7;
        if (notEmpty(parsed.skills)) present++;
        if (notEmpty(parsed.education)) present++;
        if (notEmpty(parsed.experience)) present++;
        if (notEmpty(parsed.projects)) present++;
        if (notEmpty(parsed.certifications)) present++;
        if (notEmpty(parsed.languages)) present++;
        if (notEmpty(parsed.achievements)) present++;
        return ((double) present / total) * 15.0;
    }

    private double scoreSkills(List<String> skills) {
        if (skills == null || skills.isEmpty()) return 0.0;
        int count = skills.size();
        return Math.min(10.0, (count / 8.0) * 10.0);
    }

    private double scoreExperience(List<String> experience) {
        if (experience == null || experience.isEmpty()) return 0.0;
        return experience.size() >= 3 ? 10.0 : 6.0;
    }

    private double scoreFormatting(String rawText) {
        double score = 5.0;
        Matcher matcher = LONG_LINE_PATTERN.matcher(rawText);
        int longLineCount = 0;
        while (matcher.find()) longLineCount++;
        if (longLineCount > 5) score -= 2.0;
        if (!rawText.contains("\u2022") && !rawText.matches("(?s).*[\\r\\n]-\\s.*")) score -= 1.0;
        return Math.max(0.0, score);
    }

    private double scoreActionVerbs(List<String> experience, List<String> projects) {
        int matches = 0;
        int totalLines = 0;
        for (String line : combine(experience, projects)) {
            totalLines++;
            String firstWord = line.trim().split("\\s+")[0].toLowerCase().replaceAll("[^a-z]", "");
            if (ACTION_VERBS.contains(firstWord)) matches++;
        }
        if (totalLines == 0) return 0.0;
        double ratio = (double) matches / totalLines;
        return Math.min(5.0, ratio * 10.0);
    }

    private double scoreGrammar(String rawText) {
        double score = 5.0;
        if (rawText.contains("  ")) score -= 1.0;
        if (rawText.contains("..") || rawText.contains("!!") || rawText.contains("??")) score -= 1.0;
        if (rawText.contains(" i ") || rawText.startsWith("i ")) score -= 1.0;
        return Math.max(0.0, score);
    }

    private double scoreDuplicateWords(String rawText) {
        String[] words = rawText.toLowerCase().replaceAll("[^a-z0-9\\s]", "").split("\\s+");
        if (words.length == 0) return 0.0;
        Set<String> unique = new HashSet<>(Arrays.asList(words));
        double ratio = (double) unique.size() / words.length;
        return Math.min(5.0, ratio * 6.0);
    }

    private boolean notEmpty(List<String> list) {
        return list != null && !list.isEmpty();
    }

    private List<String> combine(List<String> first, List<String> second) {
        List<String> combined = new java.util.ArrayList<>();
        if (first != null) combined.addAll(first);
        if (second != null) combined.addAll(second);
        return combined;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
