package controllers;

import model.Task;

import java.util.Arrays;
import java.util.List;

public class CSVTaskFormat {
    public static String toString(Task task) {
        return task.getId() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription();
    }

    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final String title = values[1];
        final String description = values[2];
        final String status = values[3];
        return new Task(title, description, id, status);
    }

    public static String toString(HistoryManager historyManager) {
        return historyManager.getHistory().toString();
    }

    public static List<String> historyFromString(String value) {
        final String[] values = value.split(",");
        return Arrays.stream(values).toList();
    }
}
