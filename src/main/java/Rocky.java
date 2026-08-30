import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Rocky {
    private static final Path TASK_FILE = Path.of("./data/rocky.txt");
    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    /**
     * Starts the chatbot and processes the user's task commands.
     *
     * @param args command-line arguments, which this program does not use
     */
    public static void main(String[] args) {
        String banner = " ____             _          \n"
                + "|  _ \\ ___   ___| | ___   _ \n"
                + "| |_) / _ \\ / __| |/ / | | |\n"
                + "|  _ < (_) | (__|   <| |_| |\n"
                + "|_| \\_\\___/ \\___|_|\\_\\__, |\n"
                + "                         |___/\n";
        String divider = "____________________________________________________________";

        System.out.println(divider);
        System.out.println(banner);
        System.out.println("Hello! I Rocky.");
        System.out.println("Amaze, what a special human being! What rocky do for you?");
        System.out.println(divider);
        Scanner scanner = new Scanner(System.in);

        List<Task> tasks = loadTasks();
        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine().trim();
            if (Objects.equals(userInput, "bye")) {
                break;
            }
            if (Objects.equals(userInput, "list")) {
                System.out.println("Rocky remember you have these tasks");
                System.out.println(divider);
                if (tasks.isEmpty()) {
                    System.out.println("Rocky don't see anything!");
                } else {
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + ". " + tasks.get(i));
                    }
                }
                System.out.println(divider);
                continue;
            }

            if (userInput.equals("mark") || userInput.startsWith("mark ")) {
                updateTaskStatus(userInput, "mark", true, tasks, divider);
                continue;
            }

            if (userInput.equals("unmark") || userInput.startsWith("unmark ")) {
                updateTaskStatus(userInput, "unmark", false, tasks, divider);
                continue;
            }
            if (userInput.equals("delete") || userInput.startsWith("delete ")) {
                deleteTask(userInput, tasks, divider);
                continue;
            }
            if (userInput.equals("todo") || userInput.startsWith("todo ")) {
                String description = userInput.substring("todo".length()).trim();

                if (description.isEmpty()) {
                    System.out.println(divider);
                    System.out.println("Curious? Rocky don't see description for the task...");
                    System.out.println(divider);
                } else {
                    addTask(new Todo(description), tasks, divider);
                }
                continue;
            }
            if (userInput.equals("deadline") || userInput.startsWith("deadline ")) {
                String description = userInput.substring("deadline".length()).trim();

                if (description.isEmpty()) {
                    System.out.println(divider);
                    System.out.println("Curious? Rocky don't see description for the task...");
                    System.out.println(divider);
                }
                else {
                    addDeadline(userInput, tasks, divider);
                }
                continue;
            }
            if (userInput.equals("event") || userInput.startsWith("event ")) {
                String description = userInput.substring("event".length()).trim();

                if (description.isEmpty()) {
                    System.out.println(divider);
                    System.out.println("Curious? Rocky don't see description for the task...");
                    System.out.println(divider);
                }
                else {
                    addEvent(userInput, tasks, divider);
                }
                continue;
            }
            else{
                System.out.println(divider);
                System.out.println("Rocky don't understand what this is... Try something else");
                System.out.println(divider);
            }
        }
        System.out.println("Bye. We meet again soon!");
        System.out.println(divider);
    }

    private static void updateTaskStatus(String userInput, String command, boolean completed,
                                         List<Task> tasks, String divider) {
        String taskNumberText = userInput.substring(command.length()).trim();

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            int taskIndex = taskNumber - 1;
            if (taskIndex >= 0 && taskIndex < tasks.size()) {
                Task task = tasks.get(taskIndex);
                if (completed) {
                    task.markAsDone();
                } else {
                    task.markAsNotDone();
                }
                saveTasks(tasks);
                String message = completed ? "Nice! Rocky marked this task as done:"
                        : "Oh No! Rocky marked this task as not done yet:";
                System.out.println(divider);
                System.out.println(message);
                System.out.println(task);
                System.out.println(divider);
            } else {
                System.out.println("Rocky cannot find that task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please provide a task number, for example: " + command + " 2");
        }
    }

    private static void deleteTask(String userInput, List<Task> tasks, String divider) {
        String taskNumberText = userInput.substring("delete".length()).trim();
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            int taskIndex = taskNumber - 1;
            if (taskIndex >= 0 && taskIndex < tasks.size()) {
                Task task = tasks.get(taskIndex);
                String message = "Rocky will remove that annoying task for you!:";
                System.out.println(divider);
                System.out.println(message);
                System.out.println(task);
                System.out.println(divider);
                tasks.remove(tasks.get(taskIndex));
                saveTasks(tasks);
            }else {
                System.out.println("Rocky cannot find that task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please provide a task number, for example: delete 2");
        }
    }

    private static void addTask(Task task, List<Task> tasks, String divider) {
        tasks.add(task);
        System.out.println(divider);
        System.out.println("Amaze! Rocky add this to task...:");
        System.out.println(task);
        System.out.println("Rocky see " + tasks.size() + " tasks in the list.");
        System.out.println(divider);
        saveTasks(tasks);
    }

    private static void addDeadline(String userInput, List<Task> tasks, String divider) {
        int byIndex = userInput.indexOf(" /by ");
        if (byIndex < 0) {
            System.out.println("Use: deadline DESCRIPTION /by DATE_OR_TIME");
            return;
        }
        String description = userInput.substring("deadline".length(), byIndex).trim();
        String by = userInput.substring(byIndex + " /by ".length()).trim();
        ParsedDateTime byDate = parseDateTime(by);
        if (byDate == null) {
            printDateFormatError();
            return;
        }
        addTask(byDate.hasTime
                ? new Deadline(description, byDate.value)
                : new Deadline(description, byDate.value.toLocalDate()), tasks, divider);
    }

    private static void addEvent(String userInput, List<Task> tasks, String divider) {
        int fromIndex = userInput.indexOf(" /from ");
        int toIndex = userInput.indexOf(" /to ");
        if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
            System.out.println("Use: event DESCRIPTION /from START /to END");
            return;
        }
        String description = userInput.substring("event".length(), fromIndex).trim();
        String from = userInput.substring(fromIndex + " /from ".length(), toIndex).trim();
        String to = userInput.substring(toIndex + " /to ".length()).trim();
        ParsedDateTime fromDate = parseDateTime(from);
        ParsedDateTime toDate = parseDateTime(to);
        if (fromDate == null || toDate == null) {
            printDateFormatError();
            return;
        }
        if (fromDate.hasTime != toDate.hasTime) {
            System.out.println("Use the same date or date-time format for both event dates.");
            return;
        }
        addTask(fromDate.hasTime
                ? new Event(description, fromDate.value, toDate.value)
                : new Event(description, fromDate.value.toLocalDate(), toDate.value.toLocalDate()),
                tasks, divider);
    }

    private static void saveTasks(List<Task> tasks) {
        Path temporaryFile = null;
        try {
            Path parent = TASK_FILE.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            StringBuilder fileContents = new StringBuilder();
            for (Task task : tasks) {
                fileContents.append(task.toStorageString()).append(System.lineSeparator());
            }
            Path directory = parent == null ? Path.of(".") : parent;
            temporaryFile = Files.createTempFile(directory, "rocky", ".tmp");
            Files.writeString(temporaryFile, fileContents.toString());
            try {
                Files.move(temporaryFile, TASK_FILE, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporaryFile, TASK_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException | SecurityException e) {
            System.out.println("Rocky cannot save to the file...: " + e.getMessage());
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException | SecurityException ignored) {
                    // The original save error, if any, is more useful to the user.
                }
            }
        }
    }

    /**
     * Loads saved tasks when the chatbot starts.
     *
     * @return the tasks read from disk, or an empty list when no file exists
     */
    private static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            if (Files.notExists(TASK_FILE)) {
                return tasks;
            }
            if (!Files.isRegularFile(TASK_FILE) || !Files.isReadable(TASK_FILE)) {
                System.out.println("Rocky cannot load the task file because it is not a readable file.");
                return tasks;
            }
        } catch (SecurityException e) {
            System.out.println("Rocky cannot check the task file...: " + e.getMessage());
            return tasks;
        }

        try {
            for (String line : Files.readAllLines(TASK_FILE)) {
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

    /**
     * Converts one stored line into a task.
     *
     * @param line one line from the task file
     * @return the reconstructed task, or {@code null} for an invalid line
     */
    private static Task parseTask(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] fields = line.split("\\s*\\|\\s*", -1);
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }

        try {
            if (fields.length < 2 || fields[0].isEmpty() || fields[1].isEmpty()) {
                return null;
            }
            String type = fields[0];
            boolean isDone = fields[1].equals("1");
            if (!fields[1].equals("0") && !fields[1].equals("1")) {
                return null;
            }

            Task task;
            if (type.equals("T") && fields.length == 3) {
                task = new Todo(fields[2]);
            } else if (type.equals("D") && fields.length == 4) {
                ParsedDateTime byDate = parseDateTime(fields[3]);
                if (byDate == null) {
                    return null;
                }
                task = byDate.hasTime
                        ? new Deadline(fields[2], byDate.value)
                        : new Deadline(fields[2], byDate.value.toLocalDate());
            } else if (type.equals("E") && fields.length == 5) {
                ParsedDateTime fromDate = parseDateTime(fields[3]);
                ParsedDateTime toDate = parseDateTime(fields[4]);
                if (fromDate == null || toDate == null) {
                    return null;
                }
                if (fromDate.hasTime != toDate.hasTime) {
                    return null;
                }
                task = fromDate.hasTime
                        ? new Event(fields[2], fromDate.value, toDate.value)
                        : new Event(fields[2], fromDate.value.toLocalDate(), toDate.value.toLocalDate());
            } else {
                return null;
            }
            for (int i = 2; i < fields.length; i++) {
                if (fields[i].isEmpty()) {
                    return null;
                }
            }
            if (isDone) {
                task.markAsDone();
            }
            return task;
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Parses a command date using the format accepted by Rocky.
     *
     * @param text the user-provided date
     * @return the parsed date, or {@code null} when the text is invalid
     */
    private static ParsedDateTime parseDateTime(String text) {
        try {
            if (text.contains("T")) {
                return new ParsedDateTime(LocalDateTime.parse(text), true);
            }
            if (text.contains(" ")) {
                DateTimeFormatter inputTimeFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm");
                return new ParsedDateTime(LocalDateTime.parse(text, inputTimeFormat), true);
            }
            return new ParsedDateTime(LocalDate.parse(text, INPUT_DATE_FORMAT).atStartOfDay(), false);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /** Prints the supported date and date-time input formats. */
    private static void printDateFormatError() {
        System.out.println("Use yyyy-MM-dd or yyyy-MM-dd HHmm, for example: 2019-10-15 or 2019-12-02 1800");
    }

    /** Holds a parsed date and whether the original input included a time. */
    private static class ParsedDateTime {
        private final LocalDateTime value;
        private final boolean hasTime;

        ParsedDateTime(LocalDateTime value, boolean hasTime) {
            this.value = value;
            this.hasTime = hasTime;
        }
    }
}
