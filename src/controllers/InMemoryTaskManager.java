package controllers;

import model.Epic;
import model.Statuses;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int idGenerator = 0;
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    @Override
    public ArrayList<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public int addNewTask(Task task) {
        idGenerator++;
        task.setId(idGenerator);
        tasks.put(idGenerator, task);
        return idGenerator;
    }

    @Override
    public void modifyTask(int id, Task task) {
        task.setId(id);
        tasks.replace(id, task);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        inMemoryHistoryManager.add(task);
        return task;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        idGenerator++;
        subtask.setId(idGenerator);
        subtasks.put(idGenerator, subtask);
        epics.get(subtask.getParentId()).addNewSubtaskId(subtask.getId());
        checkEpicStatus(epics.get(subtask.getParentId()));
        return idGenerator;
    }

    @Override
    public void modifySubtask(int id, Subtask subtask) {
        subtask.setId(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtasks.get(id).getParentId());
        checkEpicStatus(epic);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
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

    @Override
    public ArrayList<Subtask> getSubtasks(Epic epic) {
        ArrayList<Subtask> epicsSubtasks = new ArrayList<>();
        for (Integer id: epic.getSubtasksIds()) {
            epicsSubtasks.add(subtasks.get(id));
        }
        return epicsSubtasks;
    }

    @Override
    public void clearAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            checkEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id).getParentId());
        checkEpicStatus(epic);
        epic.deleteSubtaskById(id);
        subtasks.remove(id);
    }

    @Override
    public Task getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        inMemoryHistoryManager.add(subtask);
        return subtask;
    }

    @Override
    public int addNewEpic(Epic epic) {
        idGenerator++;
        epic.setId(idGenerator);
        epics.put(idGenerator, epic);
        return idGenerator;
    }

    @Override
    public ArrayList<Task> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void clearAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteEpicById(int id) {
        final Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtasksIds()) {
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public Task getEpicById(int id) {
        Epic epic = epics.get(id);
        inMemoryHistoryManager.add(epic);
        return epic;
    }
}