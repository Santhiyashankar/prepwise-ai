package com.prepwise.service;

public class ExecutionResult {
    public boolean compiled = true;
    public String compileError;
    public String stdout = "";
    public String stderr = "";
    public long executionTimeMs;
    public Long memoryUsageKb;
    public boolean timedOut;
}
