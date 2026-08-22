import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * A chatbot that stores and manages different types of tasks.
 */
public class Rocky {
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

        List<Task> tasks = new ArrayList<>();
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

    private static void addTask(Task task, List<Task> tasks, String divider) {
        tasks.add(task);
        System.out.println(divider);
        System.out.println("Amaze! Rocky add this to task...:");
        System.out.println(task);
        System.out.println("Rocky see " + tasks.size() + " tasks in the list.");
        System.out.println(divider);
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
}
