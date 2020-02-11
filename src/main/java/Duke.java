import java.util.Scanner;
import java.util.Arrays;

public class Duke {
    public static void main(String[] args) {
        Task[] tasks = new Task[100];
        int taskCount = 0;
        boolean isBye = false;
        greetUser();
        Scanner sc = new Scanner(System.in);
        while (!isBye) {
            String description;
            String date;
            System.out.println();
            String string = sc.nextLine();
            System.out.println("    ____________________________________________________________");
            String[] stringSplit = string.split(" ");
            try {
                switch (stringSplit[0]) {
                    case "list":
                        listCommand(tasks, stringSplit, taskCount);
                        break;
                    case "done":
                        doneCommand(tasks, stringSplit, taskCount);
                        break;
                    case "bye":
                        isBye = byeCommand(stringSplit);
                        break;
                    case "todo":
                        description = String.join(" ", Arrays.copyOfRange(stringSplit, 1, stringSplit.length));
                        tasks[taskCount] = new Todo(description);
                        System.out.println("     Got it. I've added this task: ");
                        System.out.println("       " + tasks[taskCount].toString());
                        taskCount++;
                        System.out.println("     Now you have " + taskCount + " tasks in the list.");
                        break;
                    case "deadline":
                        description = string.substring(0, string.indexOf(" /by")).replace("deadline ", "");
                        date = string.substring(string.indexOf("/by ")).replace("/by ", "");
                        tasks[taskCount] = new Deadline(description, date);
                        System.out.println("     Got it. I've added this task: ");
                        System.out.println("       " + tasks[taskCount].toString());
                        taskCount++;
                        System.out.println("     Now you have " + taskCount + " tasks in the list.");
                        break;
                    case "event":
                        description = string.substring(0, string.indexOf(" /at")).replace("event ", "");
                        date = string.substring(string.indexOf("/at ")).replace("/at ", "");
                        tasks[taskCount] = new Event(description, date);
                        System.out.println("     Got it. I've added this task: ");
                        System.out.println("       " + tasks[taskCount].toString());
                        taskCount++;
                        System.out.println("     Now you have " + taskCount + " tasks in the list.");
                        break;
                    default: //add Task into List
                        tasks[taskCount] = new Task(string);
                        System.out.println("     added: " + tasks[taskCount].getDescription());
                        taskCount++;
                        break;
                }
            } catch (DukeArgumentException e) {
                System.out.println(e.getMessage());
            } catch (DukeIndexException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("    ____________________________________________________________");
        }
    }

    public static boolean byeCommand(String[] stringSplit) throws DukeArgumentException {
        if (stringSplit.length > 1) {
            throw new DukeArgumentException("     ☹ OOPS!!! Description not required after bye.");
        }

        System.out.println("    Bye. Hope to see you again soon!");
        return true;
    }

    public static void doneCommand(Task[] tasks, String[] stringSplit, int taskCount) throws
            DukeArgumentException, DukeIndexException {
        int completedTask;
        if (stringSplit.length == 1) {
            throw new DukeArgumentException("     ☹ OOPS!!! Missing index after done.");
        } else {
            completedTask = Integer.parseInt(stringSplit[1]) - 1;
        }

        if (completedTask >= taskCount) {
            throw new DukeIndexException("     ☹ OOPS!!! Invalid index after done.");
        } else {
            System.out.println("done is: " + completedTask);
            System.out.println("taskCount is: " + taskCount);
            tasks[completedTask].markAsDone();
            System.out.println("     Nice! I've marked this task as done: ");
            System.out.println("       " + tasks[completedTask].toString());
        }
    }

    public static void listCommand(Task[] tasks, String[] stringSplit, int taskCount) throws DukeArgumentException {
        if (stringSplit.length > 1) {
            throw new DukeArgumentException("     ☹ OOPS!!! Description not required after list.");
        }

        switch (taskCount) {
        case 0:
            System.out.println("     There are currently no tasks in your list");
            break;
        default:
            System.out.println("     Here are the tasks in your list:");
            for (int i = 0; i < taskCount; i++) {
                System.out.println("     " + (i + 1) + "." + tasks[i].toString());
            }
        }
    }

    public static void greetUser() {
        System.out.println("    ____________________________________________________________");
        System.out.println("    Hello! I'm Duke");
        commandList();
        System.out.println("    What can I do for you?");
        System.out.println("    ____________________________________________________________");
    }

    public static void commandList() {
        System.out.println("    Here is the list of commands that are available:");
        System.out.println("+---------------------------------------------------------------+");
        System.out.println("| Index | Input            | Command                            |");
        System.out.println("|-------+------------------+------------------------------------|");
        System.out.println("| 01    | list             | List out all the stored task       |");
        System.out.println("|-------+------------------+------------------------------------|");
        System.out.println("| 02    | done i           | Mark task i as done                |");
        System.out.println("|-------+------------------+------------------------------------|");
        System.out.println("| 03    | bye              | Terminate the program              |");
        System.out.println("|-------+------------------+------------------------------------|");
        System.out.println("| 04    | todo j           | Add a task(j) without dateline     |");
        System.out.println("|-------+------------------+------------------------------------|");
        System.out.println("| 05    | dateline j /by d | Add a task(j) with due date d      |");
        System.out.println("|-------+------------------+------------------------------------|");
        System.out.println("| 06    | event j /at d    | Add a task(j) that start at date d |");
        System.out.println("|-------+------------------+------------------------------------|");
        System.out.println("| 07    | j                | Add a task(j)                      |");
        System.out.println("|---------------------------------------------------------------|");
    }
}
