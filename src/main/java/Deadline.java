import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate by;

    /**
     * Creates a deadline task with a description and due-time text.
     *
     * @param description the text describing the task
     * @param by the date by which the task is due
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline task in the chatbot's display format.
     *
     * @return the formatted deadline task
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + getDescription()
                + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns the type, status, description, and due time in the task file format.
     *
     * @return the storage representation of this deadline
     */
    @Override
    public String toStorageString() {
        return "D | " + getStatusValue() + " | " + getDescription() + " | " + by;
    }
}
