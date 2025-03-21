package controllers;

import exceptions.NotFoundException;
import model.Epic;
import model.Statuses;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    final HashMap<Integer, Task> tasks = new HashMap<>();
    final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    final HashMap<Integer, Epic> epics = new HashMap<>();
    private int idGenerator = 0;
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    private Set<Task> prioritizedTasks = new TreeSet<>();

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public boolean isTasksCrossed(Task newTask) {
        return prioritizedTasks.stream()
                .filter(task -> newTask.getId() != task.getId())
                .anyMatch(task -> newTask.getStartTime().isBefore(task.getEndTime())
                        && task.getStartTime().isBefore(newTask.getEndTime()));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public int addNewTask(Task task) {
        if (!isTasksCrossed(task)) {
            idGenerator++;
            task.setId(idGenerator);
            tasks.put(idGenerator, task);
            if (!task.getStartTime().isEqual(LocalDateTime.MAX)) {
                prioritizedTasks.add(task);
            }
        }
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
        tasks.keySet().forEach(inMemoryHistoryManager::remove);
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.clear();
    }

    @Override
    public void deleteTaskById(int id) {
        inMemoryHistoryManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public Task getTaskById(int id) {
        try {
            Task task = tasks.get(id);
            inMemoryHistoryManager.add(task);
            return task;
        } catch (Exception exception) {
            throw new NotFoundException("Эпик " + id + " не найден.");
        }
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        if (!isTasksCrossed(subtask)) {
            idGenerator++;
            subtask.setId(idGenerator);
            if (!subtask.getStartTime().isEqual(LocalDateTime.MAX)) {
                prioritizedTasks.add(subtask);
            }
            subtasks.put(idGenerator, subtask);
            epics.get(subtask.getParentId()).addNewSubtaskId(subtask.getId());
            checkEpicStatus(epics.get(subtask.getParentId()));
            checkEpicStartTimeAndDuration(epics.get(subtask.getParentId()));
        }
        return idGenerator;
    }

    @Override
    public void modifySubtask(int id, Subtask subtask) {
        subtask.setId(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtasks.get(id).getParentId());
        checkEpicStatus(epic);
        checkEpicStartTimeAndDuration(epic);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void checkEpicStatus(Epic epic) {
        long countNew = getAllSubtasks().stream()
                .filter(subtask -> Objects.equals(subtask.getStatus(), String.valueOf(Statuses.NEW)))
                .count();
        long countDone = getAllSubtasks().stream()
                .filter(subtask -> Objects.equals(subtask.getStatus(), String.valueOf(Statuses.DONE)))
                .count();
        if (countNew == getAllSubtasks().size()) {
            epic.setStatus(String.valueOf(Statuses.NEW));
        } else if (countDone == getAllSubtasks().size()) {
            epic.setStatus(String.valueOf(Statuses.DONE));
        } else {
            epic.setStatus(String.valueOf(Statuses.IN_PROGRESS));
        }
    }

    @Override
    public void checkEpicStartTimeAndDuration(Epic epic) {
        Duration duration = Duration.ZERO;
        LocalDateTime startTime = epic.getStartTime();
        LocalDateTime endTime = LocalDateTime.MIN;
        if (getAllSubtasks().isEmpty()) {
            return;
        }
        for (Subtask subtask : getAllSubtasks()) {
            if (startTime.isAfter(subtask.getStartTime())) {
                startTime = subtask.getStartTime();
            }
            if (endTime.isBefore(subtask.getEndTime())) {
                endTime = subtask.getEndTime();
            }
            duration = duration.plus(subtask.getDuration());
        }
        epic.setStartTime(startTime);
        epic.setDuration(duration);
        epic.setEndTime(endTime);
    }

    @Override
    public ArrayList<Subtask> getSubtasks(Epic epic) {
        ArrayList<Subtask> epicsSubtasks = new ArrayList<>();
        epicsSubtasks.addAll(
                epic.getSubtasksIds().stream()
                        .map(subtasks::get)
                        .toList()
        );
        return epicsSubtasks;
    }

    @Override
    public void clearAllSubtasks() {
        for (Integer epicId: epics.keySet()) {
            inMemoryHistoryManager.remove(epicId);
        }
        for (Epic epic : epics.values()) {
            for (Integer subtaskId: epic.getSubtasksIds()) {
                inMemoryHistoryManager.remove(subtaskId);
            }
            epic.cleanSubtaskIds();
            checkEpicStatus(epic);
            checkEpicStartTimeAndDuration(epic);
        }
        subtasks.clear();
    }

    @Override
    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id).getParentId());
        checkEpicStatus(epic);
        checkEpicStartTimeAndDuration(epic);
        epic.deleteSubtaskById(id);
        prioritizedTasks.remove(subtasks.get(id));
        inMemoryHistoryManager.remove(id);
        subtasks.remove(id);
    }

    @Override
    public Task getSubtaskById(int id) {
        try {
            Task subtask = subtasks.get(id);
            inMemoryHistoryManager.add(subtask);
            return subtask;
        } catch (Exception exception) {
            throw new NotFoundException("Эпик " + id + " не найден.");
        }
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
        prioritizedTasks.removeAll(epics.values());
        prioritizedTasks.removeAll(subtasks.values());
        epics.keySet().forEach(inMemoryHistoryManager::remove);
        subtasks.keySet().forEach(inMemoryHistoryManager::remove);
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteEpicById(int id) {
        inMemoryHistoryManager.remove(id);
        prioritizedTasks.remove(epics.get(id));
        final Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtasksIds()) {
            prioritizedTasks.remove(subtasks.get(subtaskId));
            inMemoryHistoryManager.remove(subtaskId);
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public Task getEpicById(int id) {
        try {
            Epic epic = epics.get(id);
            inMemoryHistoryManager.add(epic);
            return epic;
        } catch (Exception exception) {
            throw new NotFoundException("Эпик " + id + " не найден.");
        }
    }
}
