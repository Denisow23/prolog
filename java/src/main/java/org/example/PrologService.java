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
    private final String prologExecutable;

    public PrologService(Path knowledgeBasePath, String prologExecutable) {
        this.knowledgeBasePath = knowledgeBasePath;
        this.prologExecutable = prologExecutable;
    }

    public List<String> ask(String query) throws IOException, InterruptedException {
        String goal = String.format("(%s), write('true'), nl, fail; write('false')", query);

        ProcessBuilder pb = new ProcessBuilder(
                prologExecutable,
                "-q",
                "-s", knowledgeBasePath.toString(),
                "-g", goal,
                "-t", "halt"
        );

        final Process process;
        try {
            process = pb.start();
        } catch (IOException ex) {
            throw new IOException(buildExecutableNotFoundMessage(ex), ex);
        }

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

    private String buildExecutableNotFoundMessage(IOException ex) {
        return "Не удалось запустить SWI-Prolog ('" + prologExecutable + "').\n"
                + "Укажите корректный путь к swipl в меню Файл -> Настроить SWI-Prolog...\n"
                + "Пример для Windows: C:/Program Files/swipl/bin/swipl.exe\n"
                + "Пример для Linux/macOS: /usr/bin/swipl\n"
                + "Техническая причина: " + ex.getMessage();
    }
}
