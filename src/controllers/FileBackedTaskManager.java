package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        return taskManager;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task: getAllTasks()) {
                writer.write(task.toString() + "\n");
            }
            for (Task task: getAllSubtasks()) {
                writer.write(task.toString() + "\n");
            }
            for (Task task: getAllEpics()) {
                writer.write(task.toString() + "\n");
            }
            writer.write("\n");
            writer.write(getHistory().toString());
        } catch (IOException exception) {
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> result = super.getHistory();
        save();
        return result;
    }

    @Override
    public int addNewTask(Task task) {
        int result = super.addNewTask(task);
        save();
        return result;
    }

    @Override
    public void modifyTask(int id, Task task) {
        super.modifyTask(id, task);
        save();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> result = super.getAllTasks();
        save();
        return result;
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task result = super.getTaskById(id);
        save();
        return result;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        int result = super.addNewSubtask(subtask);
        save();
        return result;
    }

    @Override
    public void modifySubtask(int id, Subtask subtask) {
        super.modifySubtask(id, subtask);
        save();
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> result = super.getAllSubtasks();
        save();
        return result;
    }

    @Override
    public void checkEpicStatus(Epic epic) {
        super.checkEpicStatus(epic);
        save();
    }

    @Override
    public ArrayList<Subtask> getSubtasks(Epic epic) {
        ArrayList<Subtask> result = super.getSubtasks(epic);
        save();
        return result;
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public Task getSubtaskById(int id) {
        Task result = super.getSubtaskById(id);
        save();
        return result;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int result = super.addNewEpic(epic);
        save();
        return result;
    }

    @Override
    public ArrayList<Task> getAllEpics() {
        ArrayList<Task> result = super.getAllEpics();
        save();
        return result;
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public Task getEpicById(int id) {
        Task result = super.getEpicById(id);
        save();
        return result;
    }
}
