package duke.parser;

import duke.command.*;
import duke.exception.DukeArgumentException;
import duke.exception.DukeDateTimeException;
import duke.exception.DukeIndexException;
import duke.exception.DukeNullException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static duke.constant.Constant.*;

/**
 * The Parser class is in charged of parsing user input into a Command Object.
 * @author Lam Yue Wei
 * @version CS2113 AY19/20 Sem 2 Duke
 */
public class Parser {

    /**
     * Empty constructor for Parser.
     */
    public Parser() {
    }

    /**
     * Parse the user input into a Command Object based on the command keywords.
     * @param userCommand User input.
     * @return Command Object if command keyword, description and date is valid or DukeNullException otherwise.
     */
    public Command parseCommand(String userCommand) throws DukeArgumentException, DukeNullException,
            DukeIndexException, DukeDateTimeException {
        String commandKeyWord = getCommandKeyWord(userCommand);
        switch (commandKeyWord) {
        case TODO_COMMAND_KEYWORD:
            return parseTodoCommand(userCommand);
        case DEADLINE_COMMAND_KEYWORD:
            return parseDeadlineCommand(userCommand);
        case EVENT_COMMAND_KEYWORD:
            return parseEventCommand(userCommand);
        case LIST_COMMAND_KEYWORD:
            return parseListCommand(userCommand);
        case DONE_COMMAND_KEYWORD:
            return parseDoneCommand(userCommand);
        case DELETE_COMMAND_KEYWORD:
            return parseDeleteCommand(userCommand);
        case FIND_COMMAND_KEYWORD:
            return parseFindCommand(userCommand);
        case HELP_COMMAND_KEYWORD:
            return parseHelpCommand(userCommand);
        case BYE_COMMAND_KEYWORD:
            return parseByeCommand(userCommand);
        default:
            String message = "     :( OOPS!!! Command does not exist.\n";
            message += "     To view the list of commands available use the command: help";
            throw new DukeNullException(message);
        }
    }

    /**
     * Extract and return the command keyword from the user input.
     * @param userCommand User input.
     * @return String representation of Command keyword.
     */
    public String getCommandKeyWord(String userCommand) {
        String[] userCommandSplit = userCommand.split(" ");
        return userCommandSplit[0];
    }

    /**
     * Extract and return the description from the user input.
     * @param userCommand User input.
     * @return String representation of the description for the command to process.
     */
    public String getDescription(String userCommand) {
        String userCommandKeyWord = getCommandKeyWord(userCommand);
        return userCommand.substring(userCommandKeyWord.length()).strip();
    }

    /**
     * Extract and return the description from the user input using the delimiter.
     * @param userCommand User input.
     * @param delimiter Delimiter used to split the user input.
     * @return String representation of the description for the command to process.
     */
    public String getDescription(String userCommand, String delimiter) {
        String userCommandKeyWord = getCommandKeyWord(userCommand);
        return userCommand.substring(userCommandKeyWord.length(), userCommand.indexOf(delimiter)).strip();
    }

    /**
     * Extract and return the Date and Time as a String Array from the user input. String representation of date
     * and time is found in index 0 and 1 respectively.
     * @param userCommand User input.
     * @param delimiter Delimiter used to split the user input.
     * @return String Array representation of the Date and Time from the user input.
     * @throws DukeArgumentException If missing date and/or time is found or if additional parameter is found.
     */
    public String[] getDateTime(String userCommand, String delimiter) throws DukeArgumentException {
        String[] dateTime;
        String userCommandKeyword = getCommandKeyWord(userCommand);
        dateTime = userCommand.substring(userCommand.indexOf(delimiter)).replace(delimiter, "")
                .strip().split( " ");
        if (dateTime.length == 0 || dateTime.length == 1) {
            throw new DukeArgumentException("     :( OOPS!!! Missing Date or Time for " + userCommandKeyword + ".");
        }
        if (dateTime.length > 2) {
            throw new DukeArgumentException("     :( OOPS!!! Extra arguments found for " + userCommandKeyword + ".");
        }
        return dateTime;
    }

    /**
     * Handles the parsing of user command into a LocalDate Object.
     * @param userCommand User input.
     * @param delimiter Delimiter used to split the user input.
     * @return LocalDate Object of the date of the Task.
     * @throws DukeArgumentException If missing date and/or time is found or if additional parameter is found.
     * @throws DukeDateTimeException If date provided has incorrect format.
     */
    public LocalDate getDate(String userCommand, String delimiter) throws DukeArgumentException, DukeDateTimeException {
        String[] dateTime = getDateTime(userCommand, delimiter);
        try {
            return LocalDate.parse(dateTime[0]);
        } catch (DateTimeParseException e) {
            String message = "     :( OOPS!!! Please enter a valid date and time\n"
                    + "     Date and time format: YYYY-MM-DD HH:MM";
            throw new DukeDateTimeException(message);
        }
    }

    /**
     * Handles the parsing of user command into a LocalTime Object.
     * @param userCommand User input.
     * @param delimiter Delimiter used to split the user input.
     * @return LocalTime Object of the time of the Task.
     * @throws DukeArgumentException If missing date and/or time is found or if additional parameter is found.
     * @throws DukeDateTimeException If time provided has incorrect format.
     */
    public LocalTime getTime(String userCommand, String delimiter) throws DukeArgumentException, DukeDateTimeException {
        String[] dateTime = getDateTime(userCommand, delimiter);
        try {
            return LocalTime.parse(dateTime[1]);
        } catch (DateTimeParseException e) {
            String message = "     :( OOPS!!! Please enter a valid date and time\n"
                    + "     Date and time format: YYYY-MM-DD HH:MM";
            throw new DukeDateTimeException(message);
        }
    }

    /**
     * Parse userCommand as TodoCommand that add todo Task given with a description.
     * @param userCommand User input.
     * @return TodoCommand which adds Todo Task into the TaskList.
     * @throws DukeArgumentException If missing parameter for index.
     */
    public TodoCommand parseTodoCommand(String userCommand) throws DukeArgumentException {
        String description = getDescription(userCommand);
        if (description.length() == 0) {
            throw new DukeArgumentException("     :( OOPS!!! Missing description for todo.");
        }
        return new TodoCommand(description);
    }

    /**
     * Parse userCommand as DeadlineCommand that add deadline Task given a description and a due date and time.
     * @param userCommand User input.
     * @return DeadlineCommand which add deadline Task given a description and a due date.
     * @throws DukeArgumentException If there are missing description, if missing date and/or time is found or
     * if additional parameter is found.
     * @throws DukeDateTimeException If date or time provided has incorrect format.
     */
    public DeadlineCommand parseDeadlineCommand(String userCommand) throws DukeArgumentException, DukeDateTimeException {
        if (getDescription(userCommand).length() == 0) {
            throw new DukeArgumentException("     :( OOPS!!! Missing description for deadline.");
        }
        if (!userCommand.contains(DEADLINE_COMMAND_DELIMITER)) {
            throw new DukeArgumentException("     :( OOPS!!! Missing date and time for deadline.");
        }
        if (getDescription(userCommand, DEADLINE_COMMAND_DELIMITER).length() == 0) {
            throw new DukeArgumentException("     :( OOPS!!! Missing description for deadline.");
        }

        String description = getDescription(userCommand, DEADLINE_COMMAND_DELIMITER);
        LocalDate date = getDate(userCommand, DEADLINE_COMMAND_DELIMITER);
        LocalTime time = getTime(userCommand, DEADLINE_COMMAND_DELIMITER);
        return new DeadlineCommand(description, date, time);
    }

    /**
     * Parse userCommand as EventCommand that add event Task given a description and a date and time of occurrence.
     * @param userCommand User input.
     * @return EventCommand which add event Task given a description and a date and time of occurrence.
     * @throws DukeArgumentException If there are missing description, if missing date and/or time is found or
     * if additional parameter is found.
     * @throws DukeDateTimeException If date or time provided has incorrect format.
     */
    public EventCommand parseEventCommand(String userCommand) throws DukeArgumentException, DukeDateTimeException {
        if (getDescription(userCommand).length() == 0) {
            throw new DukeArgumentException("     :( OOPS!!! Missing description for event.");
        }
        if (!userCommand.contains(EVENT_COMMAND_DELIMITER)) {
            throw new DukeArgumentException("     :( OOPS!!! Missing date and time for event.");
        }
        if (getDescription(userCommand, EVENT_COMMAND_DELIMITER).length() == 0) {
            throw new DukeArgumentException("     :( OOPS!!! Missing description for event.");
        }

        String description = getDescription(userCommand, EVENT_COMMAND_DELIMITER);
        LocalDate date = getDate(userCommand, EVENT_COMMAND_DELIMITER);
        LocalTime time = getTime(userCommand, EVENT_COMMAND_DELIMITER);
        return new EventCommand(description, date, time);
    }

    /**
     * Parse userCommand as ListCommand that list all the Task stored.
     * @param userCommand User input.
     * @return ListCommand Object that provides the stored tasks.
     * @throws DukeArgumentException If additional parameter is provided.
     */
    public ListCommand parseListCommand(String userCommand) throws DukeArgumentException {
        if (getDescription(userCommand).length() > 0) {
            throw new DukeArgumentException("     :( OOPS!!! Description not required for list.");
        }
        return new ListCommand();
    }

    /**
     * Parse userCommand as DoneCommand that marks a Task as done.
     * @param userCommand User input.
     * @return DoneCommand Object that marks a Task as done.
     * @throws DukeArgumentException If missing parameter for index.
     * @throws DukeIndexException If index provided has incorrect format.
     */
    public DoneCommand parseDoneCommand(String userCommand) throws DukeArgumentException, DukeIndexException {
        if (getDescription(userCommand).length() == 0) {
            throw new DukeArgumentException("     :( OOPS!!! Missing index for done.");
        }

        try {
            int doneTask = Integer.parseInt(getDescription(userCommand)) - 1; // Might throw NumberFormatException
            return new DoneCommand(doneTask);
        } catch (NumberFormatException e) {
            throw new DukeIndexException("     :( OOPS!!! " + e.getMessage().substring(18) + " is not number!");
        }
    }

    /**
     * Parse userCommand as DeleteCommand that delete a Task.
     * @param userCommand User input.
     * @return DeleteCommand Object that delete a Task from the TaskList.
     * @throws DukeArgumentException If missing parameter for index.
     * @throws DukeIndexException If index provided has incorrect format.
     */
    public DeleteCommand parseDeleteCommand(String userCommand) throws DukeArgumentException, DukeIndexException {
        if (getDescription(userCommand).length() == 0) {
            throw new DukeArgumentException("     :( OOPS!!! Missing index for delete.");
        }
        try {
            int deleteTask = Integer.parseInt(getDescription(userCommand)) - 1; // Might throw NumberFormatException
            return new DeleteCommand(deleteTask);
        } catch (NumberFormatException e) {
            throw new DukeIndexException("     :( OOPS!!! " + e.getMessage().substring(18) + " is not number!");
        }
    }

    /**
     * Parse userCommand as FindCommand that find all Task with description that matches a key word or phrase.
     * @param userCommand User input.
     * @return FindCommand Object that find all Task with description that matches a key word or phrase.
     * @throws DukeArgumentException If missing parameter for description.
     */
    public FindCommand parseFindCommand(String userCommand) throws DukeArgumentException {
        if (getDescription(userCommand).length() == 0) {
            throw new DukeArgumentException("     :( OOPS!!! Missing description for find.");
        }
        return new FindCommand(getDescription(userCommand));
    }

    /**
     * Parse userCommand as HelpCommand that show the list of available command.
     * @param userCommand User input.
     * @return HelpCommand Object that show the list of available command.
     * @throws DukeArgumentException If additional parameter is provided.
     */
    public HelpCommand parseHelpCommand(String userCommand) throws DukeArgumentException {
        if (getDescription(userCommand).length() > 0) {
            throw new DukeArgumentException("     :( OOPS!!! Description not required for help.");
        }
        return new HelpCommand();
    }

    /**
     * Parse userCommand as ByeCommand that store all the Task into the hard disk and exit the program.
     * @param userCommand User input.
     * @return ByeCommand Object that store all the Task into the hard disk and exit the program.
     * @throws DukeArgumentException If additional parameter is provided.
     */
    public ByeCommand parseByeCommand(String userCommand) throws DukeArgumentException {
        if (getDescription(userCommand).length() > 0) {
            throw new DukeArgumentException("     :( OOPS!!! Description not required for bye.");
        }
        return new ByeCommand();
    }
}