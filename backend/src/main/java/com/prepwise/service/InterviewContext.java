package com.prepwise.service;

import java.util.ArrayList;
import java.util.List;

public class InterviewContext {
    public String role;
    public String difficulty;
    public String experience;
    public String style;
    public int questionNumber;
    public int totalQuestions;
    public List<QAPair> history = new ArrayList<>();

    public static class QAPair {
        public String question;
        public String answer;
        public double score;

        public QAPair(String question, String answer, double score) {
            this.question = question;
            this.answer = answer;
            this.score = score;
        }
    }
}
