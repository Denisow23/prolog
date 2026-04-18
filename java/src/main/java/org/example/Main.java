package org.example;

import javax.swing.*;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path kbPath = Path.of("prolog", "knowledge_base.pl");

        SwingUtilities.invokeLater(() -> {
            KnowledgeEditorFrame frame = new KnowledgeEditorFrame(kbPath);
            frame.setVisible(true);
        });
    }
}
