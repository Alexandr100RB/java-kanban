import java.util.ArrayList;
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
        task.setId(id);
        tasks.replace(id, task);
    }

    public ArrayList<String> getAllTasks() {
        ArrayList<String> allTasks = new ArrayList<>();
        for (Task task: tasks.values()) {
            allTasks.add(task.toString());
        }
        return allTasks;
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
        subtasks.put(idGenerator, subtask);
        epics.get(subtask.getParentId()).addNewSubtaskId(subtask.getId());
        checkEpicStatus(subtask.getStatus(), subtask.getParentId());
        return idGenerator;
    }

    public void modifySubtask(int id, Subtask subtask) {
        tasks.put(id, subtask);
        checkEpicStatus(subtask.getStatus(), subtask.getParentId());
    }

    public ArrayList<String> getAllSubtasks() {
        ArrayList<String> allSubtasks = new ArrayList<>();
        for (Subtask subtask: subtasks.values()) {
            allSubtasks.add(subtask.toString());
        }
        return allSubtasks;
    }

    public void checkEpicStatus(String status, int epicId) {
        for (Integer subtaskId: epics.get(epicId).getSubtasksIds()) {
            if (!subtasks.get(subtaskId).getStatus().equals(status)) {
                return;
            }
        }
        Epic epic = epics.get(epicId);
        epic.setStatus(status);
        epics.replace(epicId, epic);
    }

    public void clearAllSubtasks() {
        subtasks.clear();
        System.out.println("Все подзадачи удалены");
    }

    public void deleteSubtaskById(int id) {
        subtasks.remove(id);
        System.out.println("Подзадача " + id + " удалена");
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

    public ArrayList<String> getAllEpics() {
        ArrayList<String> allEpics = new ArrayList<>();
        for (Epic epic: epics.values()) {
            allEpics.add(epic.toString());
        }
        return allEpics;
    }

    public void clearAllEpics() {
        epics.clear();
        System.out.println("Все эпики удалены");
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
        System.out.println("Эпик " + id + " удалён");
    }

    public Task getEpicById(int id) {
        return epics.get(id);
    }
}
