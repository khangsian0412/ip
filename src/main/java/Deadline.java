/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a deadline task with a description and due-time text.
     *
     * @param description the text describing the task
     * @param by the date or time by which the task is due
     */
    public Deadline(String description, String by) {
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
        return "[D][" + getStatusIcon() + "] " + getDescription() + " (by: " + by + ")";
    }
}
