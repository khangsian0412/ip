package rocky.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_TIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma", Locale.ENGLISH);
    private final LocalDateTime by;
    private final boolean hasTime;

    /**
     * Creates a deadline task with a description and due-time text.
     *
     * @param description the text describing the task
     * @param by the date by which the task is due
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by.atStartOfDay();
        this.hasTime = false;
    }

    /**
     * Creates a deadline with a date and time.
     *
     * @param description the text describing the task
     * @param by the date and time by which the task is due
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
        this.hasTime = true;
    }

    /**
     * Returns this deadline task in the chatbot's display format.
     *
     * @return the formatted deadline task
     */
    @Override
    public String toString() {
        DateTimeFormatter format = hasTime ? DISPLAY_TIME_FORMAT : DISPLAY_FORMAT;
        return "[D][" + getStatusIcon() + "] " + getDescription()
                + " (by: " + by.format(format) + ")";
    }

    /**
     * Returns the type, status, description, and due time in the task file format.
     *
     * @return the storage representation of this deadline
     */
    @Override
    public String toStorageString() {
        String storedDate = hasTime ? by.toString() : by.toLocalDate().toString();
        return "D | " + getStatusValue() + " | " + getDescription() + " | " + storedDate;
    }
}
