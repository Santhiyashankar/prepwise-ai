package com.prepwise.service.impl;

import com.prepwise.exception.ApiException;
import com.prepwise.service.ResumeParserService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ResumeParserServiceImpl implements ResumeParserService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("(\\+?\\d{1,3}[\\s-]?)?(\\(?\\d{3,5}\\)?[\\s-]?)?\\d{3}[\\s-]?\\d{4}");
    private static final Pattern URL_PATTERN =
            Pattern.compile("(https?://)?(www\\.)?[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(/[\\w\\-./?=&%]*)?");

    private static final Map<String, String> SECTION_HEADERS = new LinkedHashMap<>();

    static {
        SECTION_HEADERS.put("SKILLS", "skills");
        SECTION_HEADERS.put("TECHNICAL SKILLS", "skills");
        SECTION_HEADERS.put("CORE SKILLS", "skills");
        SECTION_HEADERS.put("TECHNOLOGIES", "skills");
        SECTION_HEADERS.put("EDUCATION", "education");
        SECTION_HEADERS.put("ACADEMIC BACKGROUND", "education");
        SECTION_HEADERS.put("EXPERIENCE", "experience");
        SECTION_HEADERS.put("WORK EXPERIENCE", "experience");
        SECTION_HEADERS.put("PROFESSIONAL EXPERIENCE", "experience");
        SECTION_HEADERS.put("EMPLOYMENT HISTORY", "experience");
        SECTION_HEADERS.put("PROJECTS", "projects");
        SECTION_HEADERS.put("PERSONAL PROJECTS", "projects");
        SECTION_HEADERS.put("ACADEMIC PROJECTS", "projects");
        SECTION_HEADERS.put("CERTIFICATIONS", "certifications");
        SECTION_HEADERS.put("CERTIFICATES", "certifications");
        SECTION_HEADERS.put("LICENSES & CERTIFICATIONS", "certifications");
        SECTION_HEADERS.put("LANGUAGES", "languages");
        SECTION_HEADERS.put("ACHIEVEMENTS", "achievements");
        SECTION_HEADERS.put("AWARDS", "achievements");
        SECTION_HEADERS.put("AWARDS & ACHIEVEMENTS", "achievements");
    }

    @Override
    public ParsedResume parse(MultipartFile file) {
        String rawText = extractText(file);

        if (rawText == null || rawText.isBlank()) {
            throw new ApiException("Could not extract any text from this file. It may be corrupted or scanned.",
                    HttpStatus.BAD_REQUEST);
        }

        ParsedResume parsed = new ParsedResume();
        parsed.rawText = rawText;
        parsed.email = firstMatch(EMAIL_PATTERN, rawText);
        parsed.phone = firstMatch(PHONE_PATTERN, rawText);
        parsed.linkedinUrl = firstUrlContaining(rawText, "linkedin.com");
        parsed.githubUrl = firstUrlContaining(rawText, "github.com");
        parsed.portfolioUrl = firstOtherUrl(rawText);
        parsed.fullName = guessFullName(rawText);

        Map<String, List<String>> sections = splitIntoSections(rawText);
        parsed.skills = sections.getOrDefault("skills", new ArrayList<>());
        parsed.education = sections.getOrDefault("education", new ArrayList<>());
        parsed.experience = sections.getOrDefault("experience", new ArrayList<>());
        parsed.projects = sections.getOrDefault("projects", new ArrayList<>());
        parsed.certifications = sections.getOrDefault("certifications", new ArrayList<>());
        parsed.languages = sections.getOrDefault("languages", new ArrayList<>());
        parsed.achievements = sections.getOrDefault("achievements", new ArrayList<>());

        return parsed;
    }

    private String extractText(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase()
                : "";

        try {
            if ("pdf".equals(extension)) {
                try (PDDocument document = Loader.loadPDF(file.getBytes())) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    return stripper.getText(document);
                }
            } else if ("docx".equals(extension)) {
                try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
                    StringBuilder builder = new StringBuilder();
                    for (XWPFParagraph paragraph : document.getParagraphs()) {
                        builder.append(paragraph.getText()).append("\n");
                    }
                    return builder.toString();
                }
            } else {
                throw new ApiException("Unsupported file type. Please upload a PDF or DOCX file.",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException ex) {
            throw new ApiException("Failed to read the uploaded file. It may be corrupted.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private Map<String, List<String>> splitIntoSections(String rawText) {
        Map<String, List<String>> sections = new LinkedHashMap<>();
        String[] lines = rawText.split("\\r?\\n");

        String currentSection = null;
        List<String> buffer = new ArrayList<>();

        for (String line : lines) {
            String trimmed = line.trim();
            String normalized = trimmed.toUpperCase().replaceAll("[:\\-\u2013]+$", "").trim();

            if (SECTION_HEADERS.containsKey(normalized) && trimmed.length() <= 40) {
                if (currentSection != null && !buffer.isEmpty()) {
                    sections.computeIfAbsent(currentSection, key -> new ArrayList<>()).addAll(buffer);
                }
                currentSection = SECTION_HEADERS.get(normalized);
                buffer = new ArrayList<>();
                continue;
            }

            if (currentSection != null && !trimmed.isBlank()) {
                buffer.add(trimmed.replaceAll("^[\u2022*\\-\u2013]+\\s*", ""));
            }
        }

        if (currentSection != null && !buffer.isEmpty()) {
            sections.computeIfAbsent(currentSection, key -> new ArrayList<>()).addAll(buffer);
        }

        return sections;
    }

    private String guessFullName(String rawText) {
        for (String line : rawText.split("\\r?\\n")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;
            if (trimmed.length() > 60) return "";
            if (trimmed.contains("@") || trimmed.matches(".*\\d.*")) return "";
            if (SECTION_HEADERS.containsKey(trimmed.toUpperCase())) return "";
            return trimmed;
        }
        return "";
    }

    private String firstMatch(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group().trim() : null;
    }

    private String firstUrlContaining(String text, String domainFragment) {
        Matcher matcher = URL_PATTERN.matcher(text);
        while (matcher.find()) {
            String candidate = matcher.group();
            if (candidate.toLowerCase().contains(domainFragment)) {
                return normalizeUrl(candidate);
            }
        }
        return null;
    }

    private String firstOtherUrl(String text) {
        Matcher matcher = URL_PATTERN.matcher(text);
        while (matcher.find()) {
            String candidate = matcher.group().toLowerCase();
            if (!candidate.contains("linkedin.com") && !candidate.contains("github.com")
                    && !candidate.contains("@") && (candidate.contains(".com") || candidate.contains(".dev")
                    || candidate.contains(".io") || candidate.contains(".me") || candidate.contains(".in"))) {
                return normalizeUrl(matcher.group());
            }
        }
        return null;
    }

    private String normalizeUrl(String url) {
        String trimmed = url.trim();
        if (!trimmed.startsWith("http")) {
            trimmed = "https://" + trimmed;
        }
        return trimmed;
    }
}
