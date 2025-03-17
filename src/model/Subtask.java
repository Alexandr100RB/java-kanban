package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicId;
    private Types type;

    public Subtask(String title, String description, int id, String status, int epicId, LocalDateTime startTime,
                   Duration duration) {
        super(title, description, id, status, startTime, duration);
        this.epicId = epicId;
        type = Types.SUBTASK;
    }

    public Subtask(String title, String description, String status, int epicId, LocalDateTime startTime,
                   Duration duration) {
        super(title, description, status, startTime, duration);
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
                ", type=" + type +
                ", duration=" + getDuration().toMinutes() +
                ", startTime=" + getStartTime().toString() +
                ", parentTaskId=" + epicId + "}";
    }
}
