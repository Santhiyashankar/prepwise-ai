package com.prepwise.service;

import org.springframework.web.multipart.MultipartFile;

public interface ResumeParserService {

    ParsedResume parse(MultipartFile file);

    class ParsedResume {
        public String rawText;
        public String fullName;
        public String email;
        public String phone;
        public String linkedinUrl;
        public String githubUrl;
        public String portfolioUrl;
        public java.util.List<String> skills;
        public java.util.List<String> education;
        public java.util.List<String> experience;
        public java.util.List<String> projects;
        public java.util.List<String> certifications;
        public java.util.List<String> languages;
        public java.util.List<String> achievements;
    }
}
