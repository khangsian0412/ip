import java.util.Scanner;

public class Rocky {
    private static final Storage STORAGE = new Storage("./data/rocky.txt");
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

        TaskList tasks = new TaskList(STORAGE.load());
        Parser parser = new Parser();
        while (scanner.hasNextLine()) {
            Parser.Command command = parser.parse(scanner.nextLine());
            switch (command.getType()) {
            case BYE:
                System.out.println("Bye. We meet again soon!");
                System.out.println(divider);
                return;
            case LIST:
                listTasks(tasks, divider);
                break;
            case MARK:
                updateTaskStatus(command.getArgument(), "mark", true, tasks, divider);
                break;
            case UNMARK:
                updateTaskStatus(command.getArgument(), "unmark", false, tasks, divider);
                break;
            case DELETE:
                deleteTask(command.getArgument(), tasks, divider);
                break;
            case ADD:
                addTask(command.getTask(), tasks, divider);
                break;
            case MISSING_DESCRIPTION:
            case ERROR:
                showError(command, divider);
                break;
            }
        }
        System.out.println("Bye. We meet again soon!");
        System.out.println(divider);
    }

    private static void updateTaskStatus(String taskNumberText, String command, boolean completed,
                                         TaskList tasks, String divider) {
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
                STORAGE.save(tasks.asList());
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

    private static void listTasks(TaskList tasks, String divider) {
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
    }

    private static void showError(Parser.Command command, String divider) {
        if (command.showWithDivider()) {
            System.out.println(divider);
        }
        System.out.println(command.getMessage());
        if (command.showWithDivider()) {
            System.out.println(divider);
        }
    }

    private static void deleteTask(String taskNumberText, TaskList tasks, String divider) {
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
                tasks.delete(taskIndex);
                STORAGE.save(tasks.asList());
            }else {
                System.out.println("Rocky cannot find that task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please provide a task number, for example: delete 2");
        }
    }

    private static void addTask(Task task, TaskList tasks, String divider) {
        tasks.add(task);
        System.out.println(divider);
        System.out.println("Amaze! Rocky add this to task...:");
        System.out.println(task);
        System.out.println("Rocky see " + tasks.size() + " tasks in the list.");
        System.out.println(divider);
        STORAGE.save(tasks.asList());
    }

}
