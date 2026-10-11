package com.prepwise.service;

import com.prepwise.entity.CodingLanguage;

public interface CodeExecutionService {

    /**
     * Compiles (or prepares, for interpreted languages) the submitted source code exactly once.
     * The returned {@link CompiledProgram} can then be run against multiple test cases without
     * recompiling, and must be cleaned up via {@link #cleanup(CompiledProgram)} when done.
     */
    CompiledProgram compile(CodingLanguage language, String sourceCode);

    /**
     * Runs an already-compiled program against a single stdin input, enforcing the given timeout.
     */
    ExecutionResult run(CompiledProgram program, String stdin, int timeLimitMs);

    /**
     * Deletes the temporary working directory associated with a compiled program.
     */
    void cleanup(CompiledProgram program);
}
