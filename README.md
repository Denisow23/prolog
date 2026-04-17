# Лабораторная: SWI-Prolog + Java

Проект содержит:
- Базу знаний SWI-Prolog с двумя частями:
  1) дерево семейных отношений;
  2) семантическая сеть по транспорту.
- Java Swing-приложение (интерфейс + backend вызова Prolog).

## Структура
- `prolog/knowledge_base.pl` — факты, правила, примеры запросов.
- `java/src/main/java/org/example/Main.java` — точка входа.
- `java/src/main/java/org/example/KnowledgeEditorFrame.java` — GUI и меню.
- `java/src/main/java/org/example/PrologService.java` — запуск запросов в SWI-Prolog.
- `REPORT.md` — отчёт по лабораторной работе.

## Запуск
1. Установить SWI-Prolog (команда `swipl` должна быть доступна в PATH).
2. Собрать Java:
   ```bash
   javac -d out java/src/main/java/org/example/*.java
   ```
3. Запустить приложение:
   ```bash
   java -cp out org.example.Main
   ```

## Пример запуска Prolog напрямую
```bash
swipl -q -s prolog/knowledge_base.pl
```
