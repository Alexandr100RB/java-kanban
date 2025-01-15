package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getHistory();

    int addNewTask(Task task);

    void modifyTask(int id, Task task);

    ArrayList<Task> getAllTasks();

    void clearAllTasks();

    void deleteTaskById(int id);

    Task getTaskById(int id);

    int addNewSubtask(Subtask subtask);

    void modifySubtask(int id, Subtask subtask);

    ArrayList<Subtask> getAllSubtasks();

    void checkEpicStatus(Epic epic);

    ArrayList<Subtask> getSubtasks(Epic epic);

    void clearAllSubtasks();

    void deleteSubtaskById(int id);

    Task getSubtaskById(int id);

    int addNewEpic(Epic epic);

    ArrayList<Task> getAllEpics();

    void clearAllEpics();

    void deleteEpicById(int id);

    Task getEpicById(int id);
}
