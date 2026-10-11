package com.prepwise.service.impl;

import com.prepwise.service.KeywordAnalysisResult;
import com.prepwise.service.KeywordAnalysisService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class KeywordAnalysisServiceImpl implements KeywordAnalysisService {

    private static final List<String> MASTER_KEYWORDS = Arrays.asList(
            "java", "python", "javascript", "typescript", "react", "spring boot", "node.js",
            "rest api", "microservices", "sql", "mysql", "postgresql", "mongodb", "docker",
            "kubernetes", "aws", "azure", "git", "ci/cd", "agile", "scrum", "unit testing",
            "data structures", "algorithms", "system design", "machine learning", "html",
            "css", "tailwind", "redux", "graphql", "jenkins", "linux", "problem solving",
            "communication", "leadership", "team collaboration", "project management",
            "object-oriented programming", "design patterns", "cloud computing", "devops"
    );

    private static final int RECOMMENDED_LIMIT = 6;

    @Override
    public KeywordAnalysisResult analyze(String resumeText) {
        KeywordAnalysisResult result = new KeywordAnalysisResult();
        String normalizedText = resumeText == null ? "" : resumeText.toLowerCase();

        for (String keyword : MASTER_KEYWORDS) {
            int occurrences = countOccurrences(normalizedText, keyword.toLowerCase());
            if (occurrences >= 2) {
                result.matched.add(keyword);
            } else if (occurrences == 1) {
                result.weak.add(keyword);
            } else {
                result.missing.add(keyword);
            }
        }

        result.recommended = result.missing.stream()
                .limit(RECOMMENDED_LIMIT)
                .toList();

        double weightedMatches = result.matched.size() + (result.weak.size() * 0.5);
        result.skillCoveragePercentage = MASTER_KEYWORDS.isEmpty()
                ? 0.0
                : Math.round((weightedMatches / MASTER_KEYWORDS.size()) * 10000.0) / 100.0;

        return result;
    }

    private int countOccurrences(String text, String keyword) {
        Pattern pattern = Pattern.compile(Pattern.quote(keyword));
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()) count++;
        return count;
    }
}
