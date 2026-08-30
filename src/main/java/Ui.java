import java.util.Scanner;

/** Handles Rocky's console input and output. */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private final Scanner scanner;

    /** Creates a console UI connected to standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Displays Rocky's welcome message. */
    public void showWelcome() {
        String banner = " ____             _          \n"
                + "|  _ \\ ___   ___| | ___   _ \n"
                + "| |_) / _ \\ / __| |/ / | | |\n"
                + "|  _ < (_) | (__|   <| |_| |\n"
                + "|_| \\_\\___/ \\___|_|\\_\\__, |\n"
                + "                         |___/\n";
        showDivider();
        System.out.println(banner);
        System.out.println("Hello! I Rocky.");
        System.out.println("Amaze, what a special human being! What rocky do for you?");
        showDivider();
    }

    /** Returns whether another console command is available. */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /** Reads the next console command. */
    public String readLine() {
        return scanner.nextLine();
    }

    /** Displays the current task list. */
    public void showTasks(TaskList tasks) {
        System.out.println("Rocky remember you have these tasks");
        showDivider();
        if (tasks.isEmpty()) {
            System.out.println("Rocky don't see anything!");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
        showDivider();
    }

    /** Displays feedback after a task is added. */
    public void showTaskAdded(Task task, int taskCount) {
        showDivider();
        System.out.println("Amaze! Rocky add this to task...:");
        System.out.println(task);
        System.out.println("Rocky see " + taskCount + " tasks in the list.");
        showDivider();
    }

    /** Displays feedback after a task is marked or unmarked. */
    public void showTaskStatus(Task task, boolean completed) {
        showDivider();
        System.out.println(completed ? "Nice! Rocky marked this task as done:"
                : "Oh No! Rocky marked this task as not done yet:");
        System.out.println(task);
        showDivider();
    }

    /** Displays feedback before deleting a task. */
    public void showTaskDeleted(Task task) {
        showDivider();
        System.out.println("Rocky will remove that annoying task for you!:");
        System.out.println(task);
        showDivider();
    }

    /** Displays an invalid task-number message. */
    public void showInvalidTaskNumber(String command) {
        System.out.println("Please provide a task number, for example: " + command + " 2");
    }

    /** Displays a missing-task message. */
    public void showTaskNotFound() {
        System.out.println("Rocky cannot find that task number.");
    }

    /** Displays a parser error using the appropriate divider style. */
    public void showError(Parser.Command command) {
        if (command.showWithDivider()) showDivider();
        System.out.println(command.getMessage());
        if (command.showWithDivider()) showDivider();
    }

    /** Displays Rocky's goodbye message. */
    public void showBye() {
        System.out.println("Bye. We meet again soon!");
        showDivider();
    }

    private void showDivider() {
        System.out.println(DIVIDER);
    }
}
