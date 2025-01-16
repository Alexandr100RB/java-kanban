package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final ArrayList<Task> history =  new ArrayList<>();
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.size() > 9) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
