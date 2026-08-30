package rocky;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rocky.task.Task;

/** Owns the collection of tasks and its basic list operations. */
public class
TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /** Creates a task list containing a copy of the supplied tasks. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns the task at the supplied zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Removes the task at the supplied zero-based index. */
    public void delete(int index) {
        tasks.remove(index);
    }

    /** Returns whether the list contains no tasks. */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns a read-only view for persistence. */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /** Returns tasks whose descriptions contain the supplied keyword.
     *
     * @param keyword the case-insensitive text to search for.
     * @return the matching tasks in their original order.
     */
    public List<Task> find(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.matchesKeyword(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
