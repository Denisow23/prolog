package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PrologService {
    private final Path knowledgeBasePath;

    public PrologService(Path knowledgeBasePath) {
        this.knowledgeBasePath = knowledgeBasePath;
    }

    public List<String> ask(String query) throws IOException, InterruptedException {
        String goal = String.format("(%s), write('true'), nl, fail; write('false')", query);

        ProcessBuilder pb = new ProcessBuilder(
                "swipl",
                "-q",
                "-s", knowledgeBasePath.toString(),
                "-g", goal,
                "-t", "halt"
        );

        Process process = pb.start();
        int exitCode = process.waitFor();

        List<String> output = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        }

        if (exitCode != 0) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder err = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    err.append(line).append(System.lineSeparator());
                }
                throw new IOException("Ошибка выполнения SWI-Prolog:\n" + err);
            }
        }

        return output;
    }
}
