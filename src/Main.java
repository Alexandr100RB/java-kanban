import controllers.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Программа запущена!");
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Task #1", "Task #1 description", "NEW");
        taskManager.addNewTask(task1);
        Task task2 = new Task("Task #2", "Task #2 description", "NEW");
        taskManager.addNewTask(task2);
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        taskManager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1", "Subtask #1 description", "NEW", 3);
        taskManager.addNewSubtask(subtask1);
        Epic epic2 = new Epic("Epic #2", "Epic #2 description");
        taskManager.addNewEpic(epic2);
        Subtask subtask2 = new Subtask("Subtask #2", "Subtask #2 description", "NEW", 5);
        taskManager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask #3", "Subtask #3 description", "NEW", 5);
        taskManager.addNewSubtask(subtask3);
        printAllTasks(taskManager);
        printAllEpics(taskManager);
        printAllSubtasks(taskManager);
        System.out.println("---");
        taskManager.modifyTask(1, new Task("Task #1", "Task #1 new description", "IN_PROGRESS"));
        taskManager.modifyTask(2, new Task("Task #2", "Task #2 new description", "DONE"));
        taskManager.modifySubtask(4, new Subtask("Subtask #1", "Subtask #1 new description",
                subtask1.getId(), "DONE", subtask1.getParentId()));
        taskManager.modifySubtask(6, new Subtask("Subtask #2", "Subtask #2 new description",
                subtask2.getId(), "IN_PROGRESS", subtask2.getParentId()));
        taskManager.modifySubtask(7, new Subtask("Subtask #3", "Subtask #3 new description",
                subtask3.getId(), "DONE", subtask3.getParentId()));
        printAllTasks(taskManager);
        printAllEpics(taskManager);
        printAllSubtasks(taskManager);
        System.out.println("---");
        taskManager.deleteTaskById(2);
        taskManager.deleteEpicById(3);
        printAllTasks(taskManager);
        printAllEpics(taskManager);
        printAllSubtasks(taskManager);
    }

    public static void printAllTasks(TaskManager tasks) {
        for (Task task: tasks.getAllTasks()) {
            System.out.println(task);
        }
    }

    public static void printAllEpics(TaskManager epics) {
        for (Task epic: epics.getAllEpics()) {
            System.out.println(epic);
        }
    }

    public static void printAllSubtasks(TaskManager subtasks) {
        for (Task subtask: subtasks.getAllSubtasks()) {
            System.out.println(subtask);
        }
    }
}
