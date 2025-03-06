package controllers;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        //return new InMemoryTaskManager();
        return new FileBackedTaskManager(new File("task.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
