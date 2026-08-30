import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Rocky {
    private static final Path TASK_FILE = Path.of("./data/rocky.txt");
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
        addTask(new Deadline(description, by), tasks, divider);
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
        addTask(new Event(description, from, to), tasks, divider);
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
                task = new Deadline(fields[2], fields[3]);
            } else if (type.equals("E") && fields.length == 5) {
                task = new Event(fields[2], fields[3], fields[4]);
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
}
