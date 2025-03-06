package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;
    private Types type;

    public Epic(String title, String description, int id, Statuses status) {
        super(title, description, id, String.valueOf(status));
        subtaskIds = new ArrayList<>();
        type = Types.EPIC;
    }

    public Epic(String title, String description) {
        super(title, description, String.valueOf(Statuses.NEW));
        subtaskIds = new ArrayList<>();
        type = Types.EPIC;
    }

    public void addNewSubtaskId(int id) {
        subtaskIds.add(id);
    }

    public void deleteSubtaskById(int id) {
        subtaskIds.remove((Object) id);
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtaskIds;
    }

    public void cleanSubtaskIds() {
        subtaskIds.clear();
    }

    @Override
    public String toString() {
        if (subtaskIds.isEmpty()) {
            return "model.Epic={id=" + getId() +
                    ", title=" + getTitle() +
                    ", description=" + getDescription() +
                    ", status=" + getStatus() + "}";
        }
        return "model.Epic={id=" + getId() +
                ", title=" + getTitle() +
                ", description=" + getDescription() +
                ", status=" + getStatus() +
                ", subtasksIds=" + subtaskIds + "}";
    }
}
