package rocky.gui;

import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private static final int MESSAGE_LIST_SPACING = 12;
    private static final int CONTENT_SPACING = 12;
    private static final int COMMAND_BAR_SPACING = 8;
    private static final int MESSAGE_SPACING = 3;
    private static final int AVATAR_SIZE = 44;
    private static final int USER_MESSAGE_WIDTH = 220;
    private static final int ROCKY_MESSAGE_WIDTH = 560;
    private static final int WINDOW_WIDTH = 620;
    private static final int WINDOW_HEIGHT = 460;
    private static final double LATEST_MESSAGE_SCROLL_POSITION = 1.0;
    private static final String DIVIDER = "____________________________________________________________";
    private static final String TASK_FILE_PATH = "./data/rocky.txt";
    private final Storage storage = new Storage(TASK_FILE_PATH);
    private final Parser parser = new Parser();
    private final TaskList tasks = new TaskList(storage.load());
    private final VBox messageList = new VBox(MESSAGE_LIST_SPACING);
    private final ScrollPane conversation = new ScrollPane(messageList);
    private final TextField commandInput = new TextField();
    private final Image userAvatar = loadAvatar("/rocky/gui/user-avatar.jpeg");
    private final Image rockyAvatar = loadAvatar("/rocky/gui/rocky-avatar.jpeg");

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

        messageList.setPadding(new Insets(12));
        messageList.setFillWidth(true);
        conversation.setFitToWidth(true);
        conversation.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageList.heightProperty().addListener((observable, oldHeight, newHeight) ->
                Platform.runLater(() -> conversation.setVvalue(LATEST_MESSAGE_SCROLL_POSITION)));
        addRockyMessage(formatWelcome());

        commandInput.setPromptText("Enter a command, e.g. find book");
        commandInput.setOnAction(event -> processCommand());

        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> processCommand());

        HBox commandBar = new HBox(COMMAND_BAR_SPACING, commandInput, sendButton);
        HBox.setHgrow(commandInput, Priority.ALWAYS);

        VBox content = new VBox(CONTENT_SPACING, title, conversation, commandBar);
        content.setPadding(new Insets(16));
        VBox.setVgrow(conversation, Priority.ALWAYS);

        BorderPane root = new BorderPane(content);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
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
        addUserMessage(userInput);
        addRockyMessage(response);
        commandInput.clear();
        if (command.getType() == Parser.CommandType.BYE) {
            Platform.exit();
        }
    }

    /** Adds a command from the user to the conversation feed. */
    private void addUserMessage(String message) {
        messageList.getChildren().add(createMessage("You", message, userAvatar, true));
        scrollToLatestMessage();
    }

    /** Adds a response from Rocky to the conversation feed. */
    private void addRockyMessage(String message) {
        messageList.getChildren().add(createMessage("Rocky", message, rockyAvatar, false));
        scrollToLatestMessage();
    }

    /** Creates one chat message with its speaker label, text, and avatar. */
    private HBox createMessage(String sender, String message, Image avatar, boolean isUser) {
        ImageView avatarView = new ImageView(avatar);
        avatarView.setFitWidth(AVATAR_SIZE);
        avatarView.setFitHeight(AVATAR_SIZE);
        avatarView.setPreserveRatio(true);

        Label senderLabel = new Label(sender);
        senderLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4b5563;");

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(Double.MAX_VALUE);

        VBox messageBubble = new VBox(MESSAGE_SPACING, senderLabel, messageLabel);
        int messageWidth = isUser ? USER_MESSAGE_WIDTH : ROCKY_MESSAGE_WIDTH;
        messageBubble.setPrefWidth(messageWidth);
        messageBubble.setMaxWidth(messageWidth);
        messageBubble.setPadding(new Insets(10));
        messageBubble.setStyle(isUser
                ? "-fx-background-color: #dbeafe; -fx-background-radius: 12;"
                : "-fx-background-color: #fff1cc; -fx-background-radius: 12;"
                + "-fx-border-color: #c58b1b; -fx-border-width: 1.5;"
                + "-fx-border-radius: 12; -fx-effect: dropshadow(gaussian, #999999, 4, 0.2, 0, 1);");
        HBox messageRow = new HBox(COMMAND_BAR_SPACING);
        messageRow.setAlignment(Pos.TOP_RIGHT);
        if (isUser) {
            messageRow.getChildren().addAll(messageBubble, avatarView);
            messageRow.setAlignment(Pos.TOP_RIGHT);
        } else {
            messageRow.getChildren().addAll(avatarView, messageBubble);
            messageRow.setAlignment(Pos.TOP_LEFT);
        }
        return messageRow;
    }

    /** Scrolls the conversation feed to its newest message. */
    private void scrollToLatestMessage() {
        Platform.runLater(() -> {
            messageList.applyCss();
            messageList.layout();
            conversation.setVvalue(LATEST_MESSAGE_SCROLL_POSITION);
        });
    }

    /** Loads a bundled avatar image and reports a configuration error clearly. */
    private Image loadAvatar(String resourcePath) {
        if (RockyGui.class.getResource(resourcePath) == null) {
            throw new IllegalStateException("Missing GUI avatar resource: " + resourcePath);
        }
        return new Image(RockyGui.class.getResource(resourcePath).toExternalForm());
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
