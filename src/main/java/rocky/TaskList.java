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

    /** Creates a task list containing a copy of the supplied tasks.
     *
     * @param tasks the tasks to copy into this list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns the task at the supplied zero-based index.
     *
     * @param index the zero-based task index
     * @return the task at the requested index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Removes the task at the supplied zero-based index.
     *
     * @param index the zero-based task index
     */
    public void delete(int index) {
        tasks.remove(index);
    }

    /** Returns whether the list contains no tasks.
     *
     * @return {@code true} when the list contains no tasks
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** Returns the number of tasks in the list.
     *
     * @return the number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /** Returns a read-only view for persistence.
     *
     * @return an unmodifiable view of the tasks
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
