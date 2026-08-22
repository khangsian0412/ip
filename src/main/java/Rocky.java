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

        //To keep track of the status of each tasks

        List<String> statements = new ArrayList<>();
        List<Boolean> isDone = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine().trim();
            if (Objects.equals(userInput, "bye")) {
                break;
            }
            if (Objects.equals(userInput, "list")) {
                System.out.println(divider);
                if (statements.isEmpty()) {
                    System.out.println("Rocky don't see anything!");
                } else {
                    for (int i = 0; i < statements.size(); i++) {
                        String status = isDone.get(i) ? "[X]" : "[ ]";
                        System.out.println((i + 1) + ". " + status + " " + statements.get(i));
                    }
                }
                System.out.println(divider);
                continue;
            }

            if (userInput.equals("mark") || userInput.startsWith("mark ")) {
                updateTaskStatus(userInput, "mark", true, statements, isDone, divider);
                continue;
            }

            if (userInput.equals("unmark") || userInput.startsWith("unmark ")) {
                updateTaskStatus(userInput, "unmark", false, statements, isDone, divider);
                continue;
            }
            System.out.println(divider);
            System.out.println("added: " + userInput + ", but what it mean?");
            statements.add(userInput);
            isDone.add(false);
            System.out.println(divider);
        }
        System.out.println("Bye. We meet again soon!");
        System.out.println(divider);
    }

    private static void updateTaskStatus(String userInput, String command, boolean completed,
                                         List<String> statements, List<Boolean> isDone, String divider) {
        String taskNumberText = userInput.substring(command.length()).trim();

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            int taskIndex = taskNumber - 1;
            if (taskIndex >= 0 && taskIndex < statements.size()) {
                isDone.set(taskIndex, completed);
                String status = completed ? "[X]" : "[ ]";
                String message = completed ? "Nice! Rocky marked this task as done:"
                        : "Oh No! Rocky marked this task as not done yet:";
                System.out.println(divider);
                System.out.println(message);
                System.out.println(status + " " + statements.get(taskIndex));
                System.out.println(divider);
            } else {
                System.out.println("Rocky cannot find that task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please provide a task number, for example: " + command + " 2");
        }
    }
}
