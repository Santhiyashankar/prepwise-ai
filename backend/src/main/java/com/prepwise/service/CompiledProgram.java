package com.prepwise.service;

import java.nio.file.Path;
import java.util.List;

public class CompiledProgram {
    public Path workDir;
    public List<String> runCommand;
    public boolean compiled = true;
    public String compileError;
}
