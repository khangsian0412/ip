/**
 * Represents a task that occurs between a start and end time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task with a description, start-time text, and end-time text.
     *
     * @param description the text describing the event
     * @param from the date or time at which the event starts
     * @param to the date or time at which the event ends
     */
    public Event(String description, String from, String to) {
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
                + " (from: " + from + " to: " + to + ")";
    }
}
