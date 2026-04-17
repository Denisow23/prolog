package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class KnowledgeEditorFrame extends JFrame {
    private final JTextArea editorArea = new JTextArea();
    private final JTextArea outputArea = new JTextArea();
    private final Path kbPath;
    private String prologExecutable;

    public KnowledgeEditorFrame(Path kbPath) {
        super("SWI-Prolog + Java Лабораторная");
        this.kbPath = kbPath;
        this.prologExecutable = detectDefaultPrologExecutable();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        editorArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        outputArea.setEditable(false);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(editorArea),
                new JScrollPane(outputArea)
        );
        splitPane.setResizeWeight(0.7);
        add(splitPane, BorderLayout.CENTER);

        setJMenuBar(buildMenuBar());
        loadFile();
        outputArea.append("SWI-Prolog executable: " + prologExecutable + "\n");
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        JMenuItem open = new JMenuItem("Открыть");
        JMenuItem save = new JMenuItem("Сохранить");
        JMenuItem configureProlog = new JMenuItem("Настроить SWI-Prolog...");
        JMenuItem close = new JMenuItem("Закрыть");
        JMenuItem exit = new JMenuItem("Выход");

        open.addActionListener(e -> loadFile());
        save.addActionListener(e -> saveFile());
        configureProlog.addActionListener(e -> configurePrologExecutable());
        close.addActionListener(e -> editorArea.setText(""));
        exit.addActionListener(e -> dispose());

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(configureProlog);
        fileMenu.add(close);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        JMenu editMenu = new JMenu("Правка");
        JMenuItem add = new JMenuItem("Добавить");
        JMenuItem remove = new JMenuItem("Удалить");
        JMenuItem change = new JMenuItem("Изменить");

        add.addActionListener(e -> editorArea.append("\n% Новый факт или правило.\n"));
        remove.addActionListener(e -> editorArea.replaceSelection(""));
        change.addActionListener(e -> editorArea.replaceSelection("% Изменённый фрагмент"));

        editMenu.add(add);
        editMenu.add(remove);
        editMenu.add(change);

        JMenu questionsMenu = new JMenu("Вопросы");
        questionsMenu.add(createQueryItem("Запрос 1: ancestor(ilya, roman)", "ancestor(ilya, roman)"));
        questionsMenu.add(createQueryItem("Запрос 2: sibling(aleksei, karina)", "sibling(aleksei, karina)"));
        questionsMenu.add(createQueryItem("Запрос 3: has_property(tesla_model3, uses_roads)", "has_property(tesla_model3, uses_roads)"));
        questionsMenu.add(createQueryItem("Запрос 4: member_of(yamaha_r1, transport)", "member_of(yamaha_r1, transport)"));
        questionsMenu.add(createQueryItem("Запрос 5: eco_friendly(volvo_fh16)", "eco_friendly(volvo_fh16)"));
        questionsMenu.addSeparator();
        JMenuItem customQuery = new JMenuItem("Произвольный запрос...");
        customQuery.addActionListener(e -> askCustomQuery());
        questionsMenu.add(customQuery);

        JMenu helpMenu = new JMenu("Справка");
        JMenuItem about = new JMenuItem("О программе");
        about.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Демонстрационное приложение для лабораторной работы:\n" +
                        "SWI-Prolog база знаний + Java GUI/Backend.\n\n" +
                        "Если запросы не выполняются, проверьте путь к swipl\n" +
                        "в меню Файл -> Настроить SWI-Prolog...",
                "Справка",
                JOptionPane.INFORMATION_MESSAGE
        ));
        helpMenu.add(about);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(questionsMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private JMenuItem createQueryItem(String label, String query) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(e -> runQuery(query));
        return item;
    }

    private void runQuery(String query) {
        try {
            saveFile();
            PrologService service = new PrologService(kbPath, prologExecutable);
            List<String> result = service.ask(query);
            outputArea.append("?- " + query + "\n");
            result.forEach(line -> outputArea.append(line + "\n"));
            outputArea.append("------------------------------\n");
        } catch (Exception ex) {
            outputArea.append("Ошибка: " + ex.getMessage() + "\n");
            outputArea.append("------------------------------\n");
        }
    }


    private void askCustomQuery() {
        String input = JOptionPane.showInputDialog(
                this,
                "Введите Prolog-запрос без '?-' и без точки в конце:\n"
                        + "Пример: has_property(yamaha_r1, uses_roads)",
                "Произвольный запрос",
                JOptionPane.QUESTION_MESSAGE
        );

        if (input != null && !input.isBlank()) {
            runQuery(input.trim());
        }
    }

    private void configurePrologExecutable() {
        String newPath = JOptionPane.showInputDialog(
                this,
                "Введите команду или полный путь к SWI-Prolog (swipl):",
                prologExecutable
        );

        if (newPath != null && !newPath.isBlank()) {
            prologExecutable = newPath.trim();
            outputArea.append("Новый путь SWI-Prolog: " + prologExecutable + "\n");
        }
    }

    private String detectDefaultPrologExecutable() {
        String fromEnv = System.getenv("SWIPL_PATH");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv.trim();
        }
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            return "swipl.exe";
        }
        return "swipl";
    }

    private void loadFile() {
        try {
            String text = Files.readString(kbPath, StandardCharsets.UTF_8);
            editorArea.setText(text);
            outputArea.append("Открыт файл: " + kbPath + "\n");
        } catch (IOException ex) {
            outputArea.append("Не удалось открыть файл: " + ex.getMessage() + "\n");
        }
    }

    private void saveFile() {
        try {
            Files.writeString(kbPath, editorArea.getText(), StandardCharsets.UTF_8);
            outputArea.append("Сохранено: " + kbPath + "\n");
        } catch (IOException ex) {
            outputArea.append("Не удалось сохранить файл: " + ex.getMessage() + "\n");
        }
    }
}
