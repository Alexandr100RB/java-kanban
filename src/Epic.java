import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<Integer> subtaskIds; //= new ArrayList<>();

    public Epic(String title, String description, int id, Statuses status) {
        super(title, description, id, String.valueOf(status));
        subtaskIds = new ArrayList<>();
    }

    public Epic(String title, String description) {
        super(title, description, String.valueOf(Statuses.NEW));
        subtaskIds = new ArrayList<>();
    }

    public void addNewSubtaskId(int id) {
        subtaskIds.add(id);
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtaskIds;
    }

    @Override
    public String toString() {
        if (subtaskIds.isEmpty()) {
            return "Epic={id=" + getId() +
                    ", title=" + getTitle() +
                    ", description=" + getDescription() +
                    ", status=" + getStatus() + "}";
        }
        return "Epic={id=" + getId() +
                ", title=" + getTitle() +
                ", description=" + getDescription() +
                ", status=" + getStatus() +
                ", subtasksIds=" + subtaskIds + "}";
    }
}
