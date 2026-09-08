package rocky.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that occurs between a start and end time.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;
    private final boolean hasTime;

    /**
     * Creates an event task with a description, start-time text, and end-time text.
     *
     * @param description the text describing the event
     * @param from the date on which the event starts
     * @param to the date on which the event ends
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from.atStartOfDay();
        this.to = to.atStartOfDay();
        this.hasTime = false;
    }

    /**
     * Creates an event with start and end dates and times.
     *
     * @param description the text describing the event
     * @param from the date and time at which the event starts
     * @param to the date and time at which the event ends
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
        this.hasTime = true;
    }

    /**
     * Returns this event task in the chatbot's display format.
     *
     * @return the formatted event task
     */
    @Override
    public String toString() {
        DateTimeFormatter format = hasTime ? DISPLAY_DATE_TIME_FORMAT : DISPLAY_DATE_FORMAT;
        return "[E][" + getStatusIcon() + "] " + getDescription()
                + " (from: " + from.format(format)
                + " to: " + to.format(format) + ")";
    }

    /**
     * Returns the type, status, description, and times in the task file format.
     *
     * @return the storage representation of this event
     */
    @Override
    public String toStorageString() {
        String storedFrom = hasTime ? from.toString() : from.toLocalDate().toString();
        String storedTo = hasTime ? to.toString() : to.toLocalDate().toString();
        return "E | " + getStatusValue() + " | " + getDescription()
                + " | " + storedFrom + " | " + storedTo;
    }
}
