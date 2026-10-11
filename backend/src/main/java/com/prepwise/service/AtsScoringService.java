package com.prepwise.service;

public interface AtsScoringService {

    AtsScoreBreakdown computeScore(ResumeParserService.ParsedResume parsed, double keywordMatchPercentage);
}
