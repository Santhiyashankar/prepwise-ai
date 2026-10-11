package com.prepwise.service.impl;

import com.prepwise.entity.CodingLanguage;
import com.prepwise.exception.ApiException;
import com.prepwise.service.CodeExecutionService;
import com.prepwise.service.CompiledProgram;
import com.prepwise.service.ExecutionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Executes candidate-submitted code in an isolated temporary directory via subprocess.
 * Compilation happens exactly once per submission (see {@link #compile}); the resulting
 * {@link CompiledProgram} can then be run against many test cases without recompiling.
 *
 * Security notes: the subprocess environment is cleared and rebuilt from scratch so that
 * server secrets (DB credentials, JWT secret, Gemini API key, etc.) inherited from this JVM's
 * process environment are never exposed to sandboxed candidate code. Output is size-capped and
 * every run is subject to a hard wall-clock timeout.
 */
@Service
@Slf4j
public class CodeExecutionServiceImpl implements CodeExecutionService {

    private static final Pattern JAVA_CLASS_PATTERN =
            Pattern.compile("public\\s+class\\s+([A-Za-z_$][A-Za-z0-9_$]*)");
    private static final int COMPILE_TIMEOUT_MS = 10_000;
    private static final long MAX_OUTPUT_BYTES = 200_000;

    private final boolean timeCommandAvailable;

    @Value("${app.coding.executions-dir}")
    private String executionsDir;

    public CodeExecutionServiceImpl() {
        this.timeCommandAvailable = Files.exists(Path.of("/usr/bin/time"));
    }

    @Override
    public CompiledProgram compile(CodingLanguage language, String sourceCode) {
        Path workDir = createWorkDir();
        try {
            return switch (language) {
                case JAVA -> compileJava(workDir, sourceCode);
                case CPP -> compileCpp(workDir, sourceCode);
                case PYTHON -> prepareInterpreted(workDir, sourceCode, "script.py", List.of("python3", "script.py"));
                case JAVASCRIPT -> prepareInterpreted(workDir, sourceCode, "script.js", List.of("node", "script.js"));
            };
        } catch (RuntimeException ex) {
            cleanupDir(workDir);
            throw ex;
        }
    }

    @Override
    public ExecutionResult run(CompiledProgram program, String stdin, int timeLimitMs) {
        if (!program.compiled) {
            ExecutionResult result = new ExecutionResult();
            result.compiled = false;
            result.compileError = program.compileError;
            return result;
        }
        return runProcess(program.workDir, program.runCommand, stdin, timeLimitMs, true);
    }

    @Override
    public void cleanup(CompiledProgram program) {
        if (program != null && program.workDir != null) {
            cleanupDir(program.workDir);
        }
    }

    private Path createWorkDir() {
        try {
            Files.createDirectories(Path.of(executionsDir));
            return Files.createTempDirectory(Path.of(executionsDir), "run-");
        } catch (IOException ex) {
            throw new ApiException("Failed to prepare the execution sandbox", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CompiledProgram compileJava(Path workDir, String sourceCode) {
        String className = extractJavaClassName(sourceCode);
        Path sourceFile = workDir.resolve(className + ".java");
        writeSource(sourceFile, sourceCode);

        ExecutionResult compileResult = runProcess(
                workDir, List.of("javac", sourceFile.getFileName().toString()), null, COMPILE_TIMEOUT_MS, false);

        CompiledProgram program = new CompiledProgram();
        program.workDir = workDir;

        boolean classFileWasProduced = Files.exists(workDir.resolve(className + ".class"));
        if (compileResult.timedOut || (!compileResult.stderr.isBlank() && !classFileWasProduced)) {
            program.compiled = false;
            program.compileError = compileResult.stderr;
        } else {
            program.runCommand = List.of("java", "-Xss8m", "-Xmx256m", className);
        }
        return program;
    }

    private CompiledProgram compileCpp(Path workDir, String sourceCode) {
        Path sourceFile = workDir.resolve("main.cpp");
        writeSource(sourceFile, sourceCode);

        ExecutionResult compileResult = runProcess(
                workDir, List.of("g++", "-O2", "-std=c++17", "-o", "main", "main.cpp"), null, COMPILE_TIMEOUT_MS, false);

        CompiledProgram program = new CompiledProgram();
        program.workDir = workDir;

        boolean binaryWasProduced = Files.exists(workDir.resolve("main"));
        if (compileResult.timedOut || (!compileResult.stderr.isBlank() && !binaryWasProduced)) {
            program.compiled = false;
            program.compileError = compileResult.stderr;
        } else {
            program.runCommand = List.of(workDir.resolve("main").toString());
        }
        return program;
    }

    private CompiledProgram prepareInterpreted(
            Path workDir, String sourceCode, String fileName, List<String> command) {
        writeSource(workDir.resolve(fileName), sourceCode);

        CompiledProgram program = new CompiledProgram();
        program.workDir = workDir;
        program.runCommand = command;
        return program;
    }

    private void writeSource(Path sourceFile, String sourceCode) {
        try {
            Files.writeString(sourceFile, sourceCode, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new ApiException("Failed to write source file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ExecutionResult runProcess(
            Path workDir, List<String> command, String stdin, int timeoutMs, boolean measureResourceUsage) {
        ExecutionResult result = new ExecutionResult();

        List<String> finalCommand = command;
        if (measureResourceUsage && timeCommandAvailable) {
            finalCommand = new ArrayList<>();
            finalCommand.add("/usr/bin/time");
            finalCommand.add("-v");
            finalCommand.addAll(command);
        }

        ProcessBuilder builder = new ProcessBuilder(finalCommand);
        builder.directory(workDir.toFile());
        sanitizeEnvironment(builder, workDir);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        long startTime = System.nanoTime();

        try {
            Process process = builder.start();

            if (stdin != null) {
                try (OutputStream stdinStream = process.getOutputStream()) {
                    stdinStream.write(stdin.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                process.getOutputStream().close();
            }

            Future<byte[]> stdoutFuture = executor.submit(() -> readStreamLimited(process.getInputStream()));
            Future<byte[]> stderrFuture = executor.submit(() -> readStreamLimited(process.getErrorStream()));

            boolean finished = process.waitFor(timeoutMs, TimeUnit.MILLISECONDS);
            long elapsedMs = (System.nanoTime() - startTime) / 1_000_000;

            if (!finished) {
                process.destroyForcibly();
                result.timedOut = true;
                result.executionTimeMs = timeoutMs;
                result.stderr = "Execution timed out after " + timeoutMs + "ms";
                return result;
            }

            result.executionTimeMs = elapsedMs;
            result.stdout = new String(stdoutFuture.get(2, TimeUnit.SECONDS), StandardCharsets.UTF_8);
            String stderr = new String(stderrFuture.get(2, TimeUnit.SECONDS), StandardCharsets.UTF_8);

            if (measureResourceUsage && timeCommandAvailable) {
                result.memoryUsageKb = parseMaxResidentSetSize(stderr);
                result.stderr = stripTimeVerboseOutput(stderr);
            } else {
                result.stderr = stderr;
            }

            return result;
        } catch (IOException ex) {
            log.error("Failed to start process {}: {}", finalCommand, ex.getMessage());
            throw new ApiException("Failed to execute code. The required runtime may not be installed.",
                    HttpStatus.SERVICE_UNAVAILABLE);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ApiException("Code execution was interrupted", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            log.error("Unexpected error executing process: {}", ex.getMessage());
            throw new ApiException("Failed to execute code", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Clears the inherited process environment and rebuilds a minimal one. Without this, the
     * subprocess would inherit every environment variable of the Spring Boot JVM — including
     * database credentials, the JWT signing secret, and the Gemini API key — any of which a
     * malicious submission could exfiltrate simply by reading its own environment.
     */
    private void sanitizeEnvironment(ProcessBuilder builder, Path workDir) {
        Map<String, String> env = builder.environment();
        String path = env.get("PATH");
        env.clear();
        if (path != null) {
            env.put("PATH", path);
        }
        env.put("HOME", workDir.toString());
        env.put("TMPDIR", workDir.toString());
        env.put("LANG", "en_US.UTF-8");
        env.put("LC_ALL", "en_US.UTF-8");
    }

    private byte[] readStreamLimited(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int bytesRead;
        long total = 0;
        while ((bytesRead = inputStream.read(chunk)) != -1) {
            total += bytesRead;
            if (total > MAX_OUTPUT_BYTES) {
                buffer.write(chunk, 0, (int) Math.max(0, MAX_OUTPUT_BYTES - (total - bytesRead)));
                break;
            }
            buffer.write(chunk, 0, bytesRead);
        }
        return buffer.toByteArray();
    }

    private Long parseMaxResidentSetSize(String timeOutput) {
        Matcher matcher = Pattern.compile("Maximum resident set size \\(kbytes\\):\\s*(\\d+)").matcher(timeOutput);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return null;
    }

    private String stripTimeVerboseOutput(String stderr) {
        int index = stderr.indexOf("\tCommand being timed:");
        return index >= 0 ? stderr.substring(0, index).trim() : stderr;
    }

    private String extractJavaClassName(String sourceCode) {
        Matcher matcher = JAVA_CLASS_PATTERN.matcher(sourceCode);
        return matcher.find() ? matcher.group(1) : "Main";
    }

    private void cleanupDir(Path workDir) {
        try (var walk = Files.walk(workDir)) {
            walk.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ignored) {
                    // Best-effort cleanup; orphaned temp files can be purged by a maintenance job.
                }
            });
        } catch (IOException ignored) {
            // Best-effort cleanup.
        }
    }
}
