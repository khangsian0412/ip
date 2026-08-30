import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that occurs between a start and end time.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate from;
    private final LocalDate to;

    /**
     * Creates an event task with a description, start-time text, and end-time text.
     *
     * @param description the text describing the event
     * @param from the date on which the event starts
     * @param to the date on which the event ends
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event task in the chatbot's display format.
     *
     * @return the formatted event task
     */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + getDescription()
                + " (from: " + from.format(DISPLAY_FORMAT)
                + " to: " + to.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns the type, status, description, and times in the task file format.
     *
     * @return the storage representation of this event
     */
    @Override
    public String toStorageString() {
        return "E | " + getStatusValue() + " | " + getDescription()
                + " | " + from + " | " + to;
    }
}
