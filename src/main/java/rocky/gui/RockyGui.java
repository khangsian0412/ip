package rocky.gui;

import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rocky.TaskList;
import rocky.parser.Parser;
import rocky.storage.Storage;
import rocky.task.Task;

/** Provides a JavaFX interface for Rocky while reusing its existing command logic. */
public class RockyGui extends Application {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String TASK_FILE_PATH = "./data/rocky.txt";
    private final Storage storage = new Storage(TASK_FILE_PATH);
    private final Parser parser = new Parser();
    private final TaskList tasks = new TaskList(storage.load());
    private final TextArea output = new TextArea();
    private final TextField commandInput = new TextField();

    /** Creates the JavaFX application. */
    public RockyGui() {
    }

    /** Builds and displays the Rocky window.
     *
     * @param stage the primary JavaFX window.
     */
    @Override
    public void start(Stage stage) {
        Label title = new Label("Rocky");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        output.setEditable(false);
        output.setWrapText(true);
        output.setPrefRowCount(18);
        output.setText(formatWelcome());

        commandInput.setPromptText("Enter a command, e.g. find book");
        commandInput.setOnAction(event -> processCommand());

        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> processCommand());

        HBox commandBar = new HBox(8, commandInput, sendButton);
        HBox.setHgrow(commandInput, Priority.ALWAYS);

        VBox content = new VBox(12, title, output, commandBar);
        content.setPadding(new Insets(16));

        BorderPane root = new BorderPane(content);
        Scene scene = new Scene(root, 620, 460);
        stage.setTitle("Rocky Task Manager");
        stage.setScene(scene);
        stage.show();
    }

    /** Processes the command entered in the input field and updates the output area. */
    private void processCommand() {
        String userInput = commandInput.getText().trim();
        if (userInput.isEmpty()) {
            return;
        }

        Parser.Command command = parser.parse(userInput);
        String response = execute(command);
        output.appendText("\n\n> " + userInput + "\n" + response);
        commandInput.clear();
        if (command.getType() == Parser.CommandType.BYE) {
            Platform.exit();
        }
    }

    /** Executes a parsed command using the same task and storage operations as the CLI.
     *
     * @param command the parsed user command.
     * @return the response to display in the GUI.
     */
    private String execute(Parser.Command command) {
        switch (command.getType()) {
        case BYE:
            return "Bye. We meet again soon!\n" + DIVIDER;
        case LIST:
            return formatTasks(tasks.asList(), "Rocky remember you have these tasks");
        case FIND:
            return formatMatchingTasks(tasks.find(command.getArgument()));
        case ADD:
            tasks.add(command.getTask());
            storage.save(tasks.asList());
            return formatTaskAdded(command.getTask(), tasks.size());
        case MARK:
            return updateTaskStatus(command.getArgument(), "mark", true);
        case UNMARK:
            return updateTaskStatus(command.getArgument(), "unmark", false);
        case DELETE:
            return deleteTask(command.getArgument());
        case MISSING_DESCRIPTION:
        case ERROR:
            return formatError(command);
        default:
            return "Rocky cannot process that command.";
        }
    }

    /** Formats a task collection for display in the output area.
     *
     * @param taskList the tasks to format.
     * @param heading the heading shown above the tasks.
     * @return the formatted task list.
     */
    private String formatTasks(List<Task> taskList, String heading) {
        StringBuilder result = new StringBuilder(heading);
        result.append("\n").append(DIVIDER);
        if (taskList.isEmpty()) {
            result.append("\nRocky don't see anything!");
        } else {
            appendNumberedTasks(result, taskList);
        }
        return result.append("\n").append(DIVIDER).toString();
    }

    /** Formats the response for a task search using the console UI's wording. */
    private String formatMatchingTasks(List<Task> matchingTasks) {
        StringBuilder result = new StringBuilder(DIVIDER)
                .append("\nHere are the matching tasks in your list:");
        if (matchingTasks.isEmpty()) {
            result.append("\nRocky don't see any matching tasks!");
        } else {
            appendNumberedTasks(result, matchingTasks);
        }
        return result.append("\n").append(DIVIDER).toString();
    }

    /** Appends numbered tasks to a response under construction. */
    private void appendNumberedTasks(StringBuilder result, List<Task> taskList) {
        for (int i = 0; i < taskList.size(); i++) {
            result.append("\n").append(i + 1).append(". ").append(taskList.get(i));
        }
    }

    /** Formats Rocky's task-added confirmation. */
    private String formatTaskAdded(Task task, int taskCount) {
        return DIVIDER + "\nAmaze! Rocky add this to task...:\n" + task
                + "\nRocky see " + taskCount + " tasks in the list.\n" + DIVIDER;
    }

    /** Formats a parser error with the divider behavior used by the console UI. */
    private String formatError(Parser.Command command) {
        if (command.showWithDivider()) {
            return DIVIDER + "\n" + command.getMessage() + "\n" + DIVIDER;
        }
        return command.getMessage();
    }

    /** Formats Rocky's initial welcome message. */
    private String formatWelcome() {
        return DIVIDER + "\n"
                + " ____             _          \n"
                + "|  _ \\ ___   ___| | ___   _ \n"
                + "| |_) / _ \\ / __| |/ / | | |\n"
                + "|  _ < (_) | (__|   <| |_| |\n"
                + "|_| \\_\\___/ \\___|_|\\_\\__, |\n"
                + "                         |___/\n\n"
                + "Hello! I Rocky.\n"
                + "Amaze, what a special human being! What rocky do for you?\n"
                + DIVIDER;
    }

    /** Updates a task's completion status and persists the change.
     *
     * @param taskNumberText the one-based task number entered by the user.
     * @param completed the desired completion status.
     * @return the operation result for display.
     */
    private String updateTaskStatus(String taskNumberText, String command, boolean completed) {
        try {
            int taskIndex = Integer.parseInt(taskNumberText) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                return "Rocky cannot find that task number.";
            }
            Task task = tasks.get(taskIndex);
            if (completed) {
                task.markAsDone();
            } else {
                task.markAsNotDone();
            }
            storage.save(tasks.asList());
            return DIVIDER + "\n"
                    + (completed ? "Nice! Rocky marked this task as done:"
                    : "Oh No! Rocky marked this task as not done yet:")
                    + "\n" + task + "\n" + DIVIDER;
        } catch (NumberFormatException exception) {
            return "Please provide a task number, for example: " + command + " 2";
        }
    }

    /** Deletes a task and persists the updated task list.
     *
     * @param taskNumberText the one-based task number entered by the user.
     * @return the operation result for display.
     */
    private String deleteTask(String taskNumberText) {
        try {
            int taskIndex = Integer.parseInt(taskNumberText) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                return "Rocky cannot find that task number.";
            }
            Task task = tasks.get(taskIndex);
            tasks.delete(taskIndex);
            storage.save(tasks.asList());
            return DIVIDER + "\nRocky will remove that annoying task for you!:\n"
                    + task + "\n" + DIVIDER;
        } catch (NumberFormatException exception) {
            return "Please provide a task number, for example: delete 2";
        }
    }
}
