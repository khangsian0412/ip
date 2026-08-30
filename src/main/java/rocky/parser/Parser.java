package rocky.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import rocky.task.Deadline;
import rocky.task.Event;
import rocky.task.Task;
import rocky.task.Todo;

/** Converts user input into commands that Rocky can execute. */
public class Parser {
    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    /** The command categories understood by Rocky. */
    public enum CommandType {
        BYE, LIST, MARK, UNMARK, DELETE, ADD, ERROR, MISSING_DESCRIPTION
    }

    /** A parsed command and the data needed to execute it. */
    public static class Command {
        private final CommandType type;
        private final String argument;
        private final Task task;
        private final String message;
        private final boolean showWithDivider;

        private Command(CommandType type, String argument, Task task,
                        String message, boolean showWithDivider) {
            this.type = type;
            this.argument = argument;
            this.task = task;
            this.message = message;
            this.showWithDivider = showWithDivider;
        }

        public CommandType getType() { return type; }

        public String getArgument() { return argument; }

        public Task getTask() { return task; }

        public String getMessage() { return message; }

        public boolean showWithDivider() { return showWithDivider; }
    }

    /** Parses one line of user input. */
    public Command parse(String userInput) {
        String input = userInput.trim();
        if (input.equals("bye")) return command(CommandType.BYE);
        if (input.equals("list")) return command(CommandType.LIST);
        if (input.equals("mark") || input.startsWith("mark ")) {
            return command(CommandType.MARK, input.substring("mark".length()).trim());
        }
        if (input.equals("unmark") || input.startsWith("unmark ")) {
            return command(CommandType.UNMARK, input.substring("unmark".length()).trim());
        }
        if (input.equals("delete") || input.startsWith("delete ")) {
            return command(CommandType.DELETE, input.substring("delete".length()).trim());
        }
        if (input.equals("todo") || input.startsWith("todo ")) {
            String description = input.substring("todo".length()).trim();
            return description.isEmpty() ? missingDescription() : add(new Todo(description));
        }
        if (input.equals("deadline") || input.startsWith("deadline ")) {
            return parseDeadline(input);
        }
        if (input.equals("event") || input.startsWith("event ")) {
            return parseEvent(input);
        }
        return error("Rocky don't understand what this is... Try something else", true);
    }

    private Command parseDeadline(String input) {
        int byIndex = input.indexOf(" /by ");
        if (byIndex < 0) return error("Use: deadline DESCRIPTION /by DATE_OR_TIME", false);
        String description = input.substring("deadline".length(), byIndex).trim();
        if (description.isEmpty()) return missingDescription();
        ParsedDateTime by = parseDateTime(input.substring(byIndex + " /by ".length()).trim());
        if (by == null) return dateError();
        Task task = by.hasTime ? new Deadline(description, by.value)
                : new Deadline(description, by.value.toLocalDate());
        return add(task);
    }

    private Command parseEvent(String input) {
        int fromIndex = input.indexOf(" /from ");
        int toIndex = input.indexOf(" /to ");
        if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
            return error("Use: event DESCRIPTION /from START /to END", false);
        }
        String description = input.substring("event".length(), fromIndex).trim();
        if (description.isEmpty()) return missingDescription();
        ParsedDateTime from = parseDateTime(input.substring(fromIndex + " /from ".length(), toIndex).trim());
        ParsedDateTime to = parseDateTime(input.substring(toIndex + " /to ".length()).trim());
        if (from == null || to == null) return dateError();
        if (from.hasTime != to.hasTime) {
            return error("Use the same date or date-time format for both event dates.", false);
        }
        Task task = from.hasTime ? new Event(description, from.value, to.value)
                : new Event(description, from.value.toLocalDate(), to.value.toLocalDate());
        return add(task);
    }

    private ParsedDateTime parseDateTime(String text) {
        try {
            if (text.contains("T")) return new ParsedDateTime(LocalDateTime.parse(text), true);
            if (text.contains(" ")) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm");
                return new ParsedDateTime(LocalDateTime.parse(text, format), true);
            }
            return new ParsedDateTime(LocalDate.parse(text, INPUT_DATE_FORMAT).atStartOfDay(), false);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private Command command(CommandType type) {
        return new Command(type, null, null, null, false);
    }

    private Command command(CommandType type, String argument) {
        return new Command(type, argument, null, null, false);
    }

    private Command add(Task task) {
        return new Command(CommandType.ADD, null, task, null, false);
    }

    private Command missingDescription() {
        return new Command(CommandType.MISSING_DESCRIPTION, null, null,
                "Curious? Rocky don't see description for the task...", true);
    }

    private Command dateError() {
        return error("Use yyyy-MM-dd or yyyy-MM-dd HHmm, for example: 2019-10-15 or 2019-12-02 1800", false);
    }

    private Command error(String message, boolean showWithDivider) {
        return new Command(CommandType.ERROR, null, null, message, showWithDivider);
    }

    private static class ParsedDateTime {
        private final LocalDateTime value;
        private final boolean hasTime;

        ParsedDateTime(LocalDateTime value, boolean hasTime) {
            this.value = value;
            this.hasTime = hasTime;
        }
    }
}
