package rocky.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rocky.task.Deadline;
import rocky.task.Event;
import rocky.task.Todo;

/** Tests the conversion of user commands into parsed commands. */
class ParserTest {
    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    void parseSimpleCommands_returnsExpectedCommandTypes() {
        assertEquals(Parser.CommandType.BYE, parser.parse("bye").getType());
        assertEquals(Parser.CommandType.LIST, parser.parse("list").getType());
        assertEquals(Parser.CommandType.MARK, parser.parse("mark 2").getType());
        assertEquals(Parser.CommandType.UNMARK, parser.parse("unmark 3").getType());
        assertEquals(Parser.CommandType.DELETE, parser.parse("delete 1").getType());

        assertEquals("2", parser.parse("mark 2").getArgument());
        assertEquals("3", parser.parse("unmark 3").getArgument());
        assertEquals("1", parser.parse("delete 1").getArgument());
    }

    @Test
    void parseCommandsWithSurroundingWhitespace_trimsInput() {
        assertEquals(Parser.CommandType.LIST, parser.parse("  list  ").getType());
        assertEquals("2", parser.parse(" mark 2 ").getArgument());
    }

    @Test
    void parseFindCommand_returnsFindCommandWithKeyword() {
        Parser.Command command = parser.parse("find book");

        assertEquals(Parser.CommandType.FIND, command.getType());
        assertEquals("book", command.getArgument());
    }

    @Test
    void parseFindWithoutKeyword_returnsUsageError() {
        Parser.Command command = parser.parse("find");

        assertEquals(Parser.CommandType.ERROR, command.getType());
        assertEquals("Use: find KEYWORD", command.getMessage());
        assertFalse(command.showWithDivider());
    }

    @Test
    void parseTodoCommand_createsTodoWithDescription() {
        Parser.Command command = parser.parse("todo read book");

        assertEquals(Parser.CommandType.ADD, command.getType());
        assertInstanceOf(Todo.class, command.getTask());
        assertEquals("[T][ ] read book", command.getTask().toString());
        assertNull(command.getMessage());
        assertFalse(command.showWithDivider());
    }

    @Test
    void parseMissingDescription_returnsMissingDescriptionCommand() {
        Parser.Command command = parser.parse("todo");

        assertEquals(Parser.CommandType.MISSING_DESCRIPTION, command.getType());
        assertEquals("Curious? Rocky don't see description for the task...", command.getMessage());
        assertTrue(command.showWithDivider());
        assertNull(command.getTask());
    }

    @Test
    void parseDeadlineWithDate_createsDeadlineWithDate() {
        Parser.Command command = parser.parse("deadline return book /by 2019-12-02");

        assertEquals(Parser.CommandType.ADD, command.getType());
        assertInstanceOf(Deadline.class, command.getTask());
        assertEquals("[D][ ] return book (by: Dec 02 2019)", command.getTask().toString());
    }

    @Test
    void parseDeadlineWithDateAndTime_createsDeadlineWithDateAndTime() {
        Parser.Command command = parser.parse("deadline return book /by 2019-12-02 1800");

        assertEquals(Parser.CommandType.ADD, command.getType());
        assertEquals("[D][ ] return book (by: Dec 02 2019, 6:00PM)", command.getTask().toString());
    }

    @Test
    void parseDeadlineWithoutMarker_returnsUsageError() {
        Parser.Command command = parser.parse("deadline return book");

        assertEquals(Parser.CommandType.ERROR, command.getType());
        assertEquals("Use: deadline DESCRIPTION /by DATE_OR_TIME", command.getMessage());
        assertFalse(command.showWithDivider());
    }

    @Test
    void parseEventWithDates_createsEventWithDates() {
        Parser.Command command = parser.parse(
                "event project meeting /from 2019-12-02 /to 2019-12-03");

        assertEquals(Parser.CommandType.ADD, command.getType());
        assertInstanceOf(Event.class, command.getTask());
        assertEquals("[E][ ] project meeting (from: Dec 02 2019 to: Dec 03 2019)",
                command.getTask().toString());
    }

    @Test
    void parseEventWithDateAndTimes_createsEventWithDateAndTimes() {
        Parser.Command command = parser.parse(
                "event project meeting /from 2019-12-02 1800 /to 2019-12-02 2000");

        assertEquals(Parser.CommandType.ADD, command.getType());
        assertEquals("[E][ ] project meeting (from: Dec 02 2019, 6:00PM"
                        + " to: Dec 02 2019, 8:00PM)", command.getTask().toString());
    }

    @Test
    void parseEventWithMixedDateFormats_returnsFormatError() {
        Parser.Command command = parser.parse(
                "event project meeting /from 2019-12-02 /to 2019-12-03 1800");

        assertEquals(Parser.CommandType.ERROR, command.getType());
        assertEquals("Use the same date or date-time format for both event dates.",
                command.getMessage());
    }

    @Test
    void parseInvalidDateOrEventSyntax_returnsUsefulError() {
        Parser.Command invalidDate = parser.parse("deadline return book /by 02-12-2019");
        Parser.Command invalidEvent = parser.parse("event project meeting /from 2019-12-02");

        assertEquals(Parser.CommandType.ERROR, invalidDate.getType());
        assertEquals("Use yyyy-MM-dd or yyyy-MM-dd HHmm, for example: 2019-10-15"
                        + " or 2019-12-02 1800", invalidDate.getMessage());
        assertEquals(Parser.CommandType.ERROR, invalidEvent.getType());
        assertEquals("Use: event DESCRIPTION /from START /to END", invalidEvent.getMessage());
    }

    @Test
    void parseUnknownCommand_returnsErrorWithDivider() {
        Parser.Command command = parser.parse("archive everything");

        assertEquals(Parser.CommandType.ERROR, command.getType());
        assertEquals("Rocky don't understand what this is... Try something else",
                command.getMessage());
        assertTrue(command.showWithDivider());
        assertNotNull(command);
    }
}
