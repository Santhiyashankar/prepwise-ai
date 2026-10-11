package com.prepwise.service;

import java.util.ArrayList;
import java.util.List;

public class GDContext {
    public String topic;
    public String category;
    public String difficulty;
    public List<ParticipantInfo> participants = new ArrayList<>();
    public List<TranscriptEntry> transcript = new ArrayList<>();

    public static class ParticipantInfo {
        public String name;
        public String personality;

        public ParticipantInfo(String name, String personality) {
            this.name = name;
            this.personality = personality;
        }
    }

    public static class TranscriptEntry {
        public String senderName;
        public String content;

        public TranscriptEntry(String senderName, String content) {
            this.senderName = senderName;
            this.content = content;
        }
    }
}
