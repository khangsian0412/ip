package rocky.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import rocky.task.Deadline;
import rocky.task.Event;
import rocky.task.Task;
import rocky.task.Todo;

/** Tests persistence and reconstruction of the supported task formats. */
class StorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void saveAndLoad_roundTripsAllTaskTypesAndCompletionStatus() throws Exception {
        Path taskFile = temporaryDirectory.resolve("nested/data/rocky.txt");
        Storage storage = new Storage(taskFile.toString());
        Todo todo = new Todo("read book");
        todo.markAsDone();
        Deadline dateDeadline = new Deadline("return book", LocalDate.of(2019, 12, 2));
        Deadline timedDeadline = new Deadline("submit report",
                LocalDateTime.of(2019, 12, 3, 18, 0));
        Event dateEvent = new Event("holiday", LocalDate.of(2019, 12, 4),
                LocalDate.of(2019, 12, 5));
        Event timedEvent = new Event("project meeting",
                LocalDateTime.of(2019, 12, 6, 18, 0),
                LocalDateTime.of(2019, 12, 6, 20, 0));

        storage.save(List.of(todo, dateDeadline, timedDeadline, dateEvent, timedEvent));

        assertTrue(Files.isRegularFile(taskFile));
        List<Task> loaded = storage.load();
        assertEquals(5, loaded.size());
        assertEquals("[T][X] read book", loaded.get(0).toString());
        assertEquals("[D][ ] return book (by: Dec 02 2019)", loaded.get(1).toString());
        assertEquals("[D][ ] submit report (by: Dec 03 2019, 6:00PM)",
                loaded.get(2).toString());
        assertEquals("[E][ ] holiday (from: Dec 04 2019 to: Dec 05 2019)",
                loaded.get(3).toString());
        assertEquals("[E][ ] project meeting (from: Dec 06 2019, 6:00PM"
                        + " to: Dec 06 2019, 8:00PM)", loaded.get(4).toString());
    }

    @Test
    void load_whenFileDoesNotExist_returnsEmptyList() {
        Storage storage = new Storage(temporaryDirectory.resolve("missing.txt").toString());

        assertTrue(storage.load().isEmpty());
    }

    @Test
    void save_emptyTaskList_createsEmptyFile() throws Exception {
        Path taskFile = temporaryDirectory.resolve("empty/rocky.txt");
        Storage storage = new Storage(taskFile.toString());

        storage.save(List.of());

        assertTrue(Files.isRegularFile(taskFile));
        assertEquals("", Files.readString(taskFile));
    }

    @Test
    void load_ignoresBlankMalformedAndUnsupportedRecords() throws Exception {
        Path taskFile = temporaryDirectory.resolve("rocky.txt");
        Files.writeString(taskFile, String.join(System.lineSeparator(),
                "",
                "T | 0 | valid task",
                "not a task",
                "T | 2 | invalid status",
                "D | 0 | bad date | 2019-99-99",
                "E | 0 | mixed formats | 2019-12-02 | 2019-12-03 1800",
                "X | 0 | unsupported type",
                "T | 0 | another valid task",
                ""));
        Storage storage = new Storage(taskFile.toString());

        List<Task> loaded = storage.load();

        assertEquals(2, loaded.size());
        assertEquals("[T][ ] valid task", loaded.get(0).toString());
        assertEquals("[T][ ] another valid task", loaded.get(1).toString());
    }

    @Test
    void save_replacesExistingContents() throws Exception {
        Path taskFile = temporaryDirectory.resolve("rocky.txt");
        Storage storage = new Storage(taskFile.toString());
        storage.save(List.of(new Todo("old task")));

        storage.save(List.of(new Todo("new task")));

        String contents = Files.readString(taskFile);
        assertTrue(contents.contains("new task"));
        assertFalse(contents.contains("old task"));
    }
}
