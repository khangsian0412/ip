import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Rocky {
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
            System.out.println(divider);
            System.out.println("added: " + userInput + ", but what it mean?");
            tasks.add(new Task(userInput));
            System.out.println(divider);
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
}
