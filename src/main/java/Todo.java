/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {
    /**
     * Creates a to-do task with the given description.
     *
     * @param description the text describing the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this to-do task in the chatbot's display format.
     *
     * @return the formatted to-do task
     */
    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + getDescription();
    }

    /**
     * Returns the type, status, and description in the task file format.
     *
     * @return the storage representation of this to-do
     */
    @Override
    public String toStorageString() {
        return "T | " + getStatusValue() + " | " + getDescription();
    }
}
