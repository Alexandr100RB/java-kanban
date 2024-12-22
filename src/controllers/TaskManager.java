package controllers;

import model.Epic;
import model.Statuses;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int idGenerator = 0;

    public int addNewTask(Task task) {
        idGenerator++;
        task.setId(idGenerator);
        tasks.put(idGenerator, task);
        return idGenerator;
    }

    public void modifyTask(int id, Task task) {
        task.setId(id);
        tasks.replace(id, task);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public int addNewSubtask(Subtask subtask) {
        idGenerator++;
        subtask.setId(idGenerator);
        subtasks.put(idGenerator, subtask);
        epics.get(subtask.getParentId()).addNewSubtaskId(subtask.getId());
        checkEpicStatus(epics.get(subtask.getParentId()));
        return idGenerator;
    }

    public void modifySubtask(int id, Subtask subtask) {
        subtask.setId(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtasks.get(id).getParentId());
        checkEpicStatus(epic);
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void checkEpicStatus(Epic epic) {
        int newStat = 0;
        for (Subtask subtask : getAllSubtasks()) {
            if (Objects.equals(subtask.getStatus(), String.valueOf(Statuses.IN_PROGRESS))) {
                epic.setStatus(String.valueOf(Statuses.IN_PROGRESS));
                return;
            }
            if (Objects.equals(subtask.getStatus(), String.valueOf(Statuses.NEW))) {
                newStat++;
            }
        }
        if (newStat == getAllSubtasks().size()) {
            epic.setStatus(String.valueOf(Statuses.NEW));
        } else {
            epic.setStatus(String.valueOf(Statuses.DONE));
        }
    }

    public ArrayList<Subtask> getSubtasks(Epic epic) {
        ArrayList<Subtask> epicsSubtasks = new ArrayList<>();
        for (Integer id: epic.getSubtasksIds()) {
            epicsSubtasks.add(subtasks.get(id));
        }
        return epicsSubtasks;
    }

    public void clearAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            checkEpicStatus(epic);
        }
        subtasks.clear();
    }

    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id).getParentId());
        checkEpicStatus(epic);
        epic.deleteSubtaskById(id);
        subtasks.remove(id);
    }

    public Task getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public int addNewEpic(Epic epic) {
        idGenerator++;
        epic.setId(idGenerator);
        epics.put(idGenerator, epic);
        return idGenerator;
    }

    public ArrayList<Task> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void clearAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteEpicById(int id) {
        final Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtasksIds()) {
            subtasks.remove(subtaskId);
        }
    }

    public Task getEpicById(int id) {
        return epics.get(id);
    }
}
