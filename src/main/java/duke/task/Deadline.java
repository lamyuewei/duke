package duke.task;

public class Deadline extends Task {
    String date;
    public Deadline(String description, String date) {
        super(description);
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.date + ")";
    }
}
