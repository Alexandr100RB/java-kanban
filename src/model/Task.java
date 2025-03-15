package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task>{
    private String title;
    private String description;
    private int id;
    private String status;
    private Types type;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String title, String description, int id, String status, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        type = Types.TASK;
    }

    public Task(String title, String description, String status, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        type = Types.TASK;
    }

    public Task(String title, String description, int id, String status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = LocalDateTime.MAX;
        this.duration = Duration.ZERO;
        type = Types.TASK;
    }

    public Task(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = LocalDateTime.MAX;
        this.duration = Duration.ZERO;
        type = Types.TASK;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Types getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id && Objects.equals(title, task.title) &&
                Objects.equals(status, task.status) &&
                Objects.equals(description, task.description);
    }

    @Override
    public String toString() {
        return "model.Task={id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", status=" + status +
                ", type=" + type +
                ", duration=" + duration.toMinutes() +
                ", startTime=" + startTime.toString() + "}";
    }

    @Override
    public int compareTo(Task o) {
        return this.startTime.compareTo(o.startTime);
    }
}