package com.prepwise.service;

import java.util.ArrayList;
import java.util.List;

public class KeywordAnalysisResult {
    public List<String> matched = new ArrayList<>();
    public List<String> weak = new ArrayList<>();
    public List<String> missing = new ArrayList<>();
    public List<String> recommended = new ArrayList<>();
    public double skillCoveragePercentage;
}
