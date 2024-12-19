import java.util.HashMap;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String title, String description, int id, Statuses status) {
        super(title, description, id, String.valueOf(status));
    }

    public Subtask(String title, String description, String status) {
        super(title, description, status);
    }
}
