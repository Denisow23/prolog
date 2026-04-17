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
1. Установить SWI-Prolog.
2. Убедиться, что `swipl` доступен в PATH.
   - Linux/macOS: обычно `/usr/bin/swipl`
   - Windows: обычно `C:\Program Files\swipl\bin\swipl.exe`
3. Собрать Java:
   ```bash
   javac -d out java/src/main/java/org/example/*.java
   ```
4. Запустить приложение:
   ```bash
   java -cp out org.example.Main
   ```

## Если приложение пишет «Cannot run program swipl»
1. Откройте меню **Файл → Настроить SWI-Prolog...**.
2. Укажите полный путь к исполняемому файлу `swipl`.
3. Повторите запрос из меню **Вопросы**.

Также можно задать путь через переменную окружения `SWIPL_PATH`.

## Пример запуска Prolog напрямую
```bash
swipl -q -s prolog/knowledge_base.pl
```
