import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String title, String description, int id, Statuses status) {
        super(title, description, id, String.valueOf(status));
    }

    public Epic(String title, String description, String status) {
        super(title, description, status);
    }

}
