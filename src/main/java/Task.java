/**
 * Represents one task and whether it has been completed.
 */
public class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }


    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns this task's status icon and description for display.
     *
     * @return the formatted task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }
}
