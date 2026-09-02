package rocky.storage;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import rocky.task.Deadline;
import rocky.task.Event;
import rocky.task.Task;
import rocky.task.Todo;

/**
 * Handles loading tasks from and saving tasks to a file.
 */
public class Storage {
    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private final Path taskFile;

    /**
     * Creates storage for the supplied task file path.
     *
     * @param filePath the path of the task file.
     */
    public Storage(String filePath) {
        this.taskFile = Path.of(filePath);
    }

    /**
     * Saves all tasks, creating the parent directory when necessary.
     *
     * @param tasks the tasks to persist.
     */
    public void save(List<Task> tasks) {
        Path temporaryFile = null;
        try {
            Path parent = taskFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            StringBuilder contents = new StringBuilder();
            for (Task task : tasks) {
                contents.append(task.toStorageString()).append(System.lineSeparator());
            }
            Path directory = parent == null ? Path.of(".") : parent;
            temporaryFile = Files.createTempFile(directory, "rocky", ".tmp");
            Files.writeString(temporaryFile, contents.toString());
            try {
                Files.move(temporaryFile, taskFile, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporaryFile, taskFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException | SecurityException e) {
            System.out.println("Rocky cannot save to the file...: " + e.getMessage());
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException | SecurityException ignored) {
                    // Keep the original save error as the useful message.
                }
            }
        }
    }

    /**
     * Loads valid tasks, returning an empty list when no readable file exists.
     *
     * @return the valid tasks reconstructed from the task file.
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try {
            if (Files.notExists(taskFile)) {
                return tasks;
            }
            if (!Files.isRegularFile(taskFile) || !Files.isReadable(taskFile)) {
                System.out.println("Rocky cannot load the task file because it is not a readable file.");
                return tasks;
            }
            for (String line : Files.readAllLines(taskFile)) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException | SecurityException e) {
            System.out.println("Rocky cannot load from the file...: " + e.getMessage());
        }
        return tasks;
    }

    /** Reconstructs one task from its stored pipe-delimited representation.
     *
     * @param line the stored task line.
     * @return the reconstructed task, or {@code null} when the line is invalid.
     */
    private Task parseTask(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] fields = line.split("\\s*\\|\\s*", -1);
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }
        try {
            if (fields.length < 2 || fields[0].isEmpty() || fields[1].isEmpty()
                    || (!fields[1].equals("0") && !fields[1].equals("1"))) {
                return null;
            }
            Task task;
            if (fields[0].equals("T") && fields.length == 3 && !fields[2].isEmpty()) {
                task = new Todo(fields[2]);
            } else if (fields[0].equals("D") && fields.length == 4 && !fields[2].isEmpty()) {
                ParsedDateTime by = parseDateTime(fields[3]);
                if (by == null) {
                    return null;
                }
                task = by.hasTime ? new Deadline(fields[2], by.value)
                        : new Deadline(fields[2], by.value.toLocalDate());
            } else if (fields[0].equals("E") && fields.length == 5 && !fields[2].isEmpty()) {
                ParsedDateTime from = parseDateTime(fields[3]);
                ParsedDateTime to = parseDateTime(fields[4]);
                if (from == null || to == null || from.hasTime != to.hasTime) {
                    return null;
                }
                task = from.hasTime ? new Event(fields[2], from.value, to.value)
                        : new Event(fields[2], from.value.toLocalDate(), to.value.toLocalDate());
            } else {
                return null;
            }
            if (fields[1].equals("1")) {
                task.markAsDone();
            }
            return task;
        } catch (RuntimeException e) {
            return null;
        }
    }

    /** Parses a stored date or date-time value into the internal representation.
     *
     * @param text the stored date or date-time text
     * @return the parsed value, or {@code null} when the text is invalid
     */
    private ParsedDateTime parseDateTime(String text) {
        try {
            if (text.contains("T")) {
                return new ParsedDateTime(LocalDateTime.parse(text), true);
            }
            if (text.contains(" ")) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm");
                return new ParsedDateTime(LocalDateTime.parse(text, format), true);
            }
            return new ParsedDateTime(LocalDate.parse(text, INPUT_DATE_FORMAT).atStartOfDay(), false);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static class ParsedDateTime {
        private final LocalDateTime value;
        private final boolean hasTime;

        /** Creates a parsed date-time and records whether the input included a time.
         *
         * @param value the parsed date-time value
         * @param hasTime whether the original input included a time
         */
        ParsedDateTime(LocalDateTime value, boolean hasTime) {
            this.value = value;
            this.hasTime = hasTime;
        }
    }
}
