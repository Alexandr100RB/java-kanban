package controllers;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private int idCounter;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        if (!Files.exists(file.toPath())) {
            try {
                Files.createFile(file.toPath());
            } catch (Exception exception) {
                System.out.println("Не удалось создать файл");
            }
        }
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                Task task = CSVTaskFormat.taskFromString(line);
                if (task.getId() > taskManager.idCounter) {
                    taskManager.idCounter = task.getId();
                }
                switch (task.getType()) {
                    case TASK:
                        taskManager.tasks.put(task.getId(), task);
                        break;
                    case SUBTASK:
                        taskManager.subtasks.put(task.getId(), (Subtask) task);
                        break;
                    case EPIC:
                        taskManager.epics.put(task.getId(), (Epic) task);
                        break;
                }
            }
            if (!taskManager.getAllSubtasks().isEmpty()) {
                for (Subtask subtask : taskManager.getAllSubtasks()) {
                    Epic epic = (Epic) taskManager.getAllEpics().get(subtask.getParentId());
                    epic.getSubtasksIds().add(subtask.getId());
                }
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
        return taskManager;
    }

    public void save() throws ManagerSaveException {
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
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> result = super.getHistory();
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
        return result;
    }

    @Override
    public void checkEpicStatus(Epic epic) {
        super.checkEpicStatus(epic);
        save();
    }

    @Override
    public void checkEpicStartTimeAndDuration(Epic epic) {
        super.checkEpicStartTimeAndDuration(epic);
        save();
    };

    @Override
    public ArrayList<Subtask> getSubtasks(Epic epic) {
        ArrayList<Subtask> result = super.getSubtasks(epic);
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
