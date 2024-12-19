import java.util.HashMap;

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
        tasks.put(id, task);
    }

    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }

    public void clearAllTasks() {
        tasks.clear();
        System.out.println("Все задачи удалены");
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
        System.out.println("Задача " + id + " удалена");
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public int addNewSubtask(Subtask subtask) {
        idGenerator++;
        subtask.setId(idGenerator);
        tasks.put(idGenerator, subtask);
        return idGenerator;
    }

    public void modifySubtask(int id, Subtask subtask) {
        tasks.put(id, subtask);
    }

    public HashMap<Integer, Subtask> getAllSubtasks() {
        return subtasks;
    }

    public void clearAllSubtasks() {
        subtasks.clear();
        System.out.println("Все задачи удалены");
    }

    public void deleteSubtaskById(int id) {
        subtasks.remove(id);
        System.out.println("Задача " + id + " удалена");
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

    public void modifyEpic(int id, Epic epic) {
        tasks.put(id, epic);
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }

    public void clearAllEpics() {
        epics.clear();
        System.out.println("Все задачи удалены");
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
        System.out.println("Задача " + id + " удалена");
    }

    public Task getEpicById(int id) {
        return epics.get(id);
    }
}
