/**
 * Represents the common state and behavior of all task types.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    protected Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns this task's description for subclasses that format the task.
     *
     * @return the task description
     */
    protected String getDescription() {
        return description;
    }

    /**
     * Returns the icon representing this task's completion status.
     *
     * @return {@code "X"} when the task is done, otherwise a space
     */
    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        isDone = false;
    }
}
