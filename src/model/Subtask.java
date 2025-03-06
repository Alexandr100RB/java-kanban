package model;

public class Subtask extends Task {

    private int epicId;
    private Types type;

    public Subtask(String title, String description, int id, String status, int epicId) {
        super(title, description, id, status);
        this.epicId = epicId;
        type = Types.SUBTASK;
    }

    public Subtask(String title, String description, String status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
        type = Types.SUBTASK;
    }

    public int getParentId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "model.Subtask={id=" + getId() +
                ", title=" + getTitle() +
                ", description=" + getDescription() +
                ", status=" + getStatus() +
                ", parentTaskId=" + epicId + "}";
    }
}
