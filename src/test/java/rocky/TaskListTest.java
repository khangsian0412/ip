package rocky;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import rocky.task.Task;
import rocky.task.Todo;

/** Tests the core collection operations provided by {@link TaskList}. */
class TaskListTest {
    @Test
    void emptyList_reportsNoTasks() {
        TaskList tasks = new TaskList();

        assertTrue(tasks.isEmpty());
        assertEquals(0, tasks.size());
    }

    @Test
    void addGetAndDelete_maintainTaskOrder() {
        Task first = new Todo("first");
        Task second = new Todo("second");
        TaskList tasks = new TaskList();

        tasks.add(first);
        tasks.add(second);

        assertEquals(2, tasks.size());
        assertEquals(first, tasks.get(0));
        assertEquals(second, tasks.get(1));

        tasks.delete(0);

        assertEquals(1, tasks.size());
        assertEquals(second, tasks.get(0));
    }

    @Test
    void constructor_copiesSuppliedList() {
        List<Task> original = List.of(new Todo("saved task"));
        TaskList tasks = new TaskList(original);

        assertEquals(1, tasks.size());
        assertEquals("[T][ ] saved task", tasks.get(0).toString());
    }

    @Test
    void asList_doesNotAllowExternalModification() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("protected task"));

        assertThrows(UnsupportedOperationException.class,
                () -> tasks.asList().clear());
        assertEquals(1, tasks.size());
    }
}
